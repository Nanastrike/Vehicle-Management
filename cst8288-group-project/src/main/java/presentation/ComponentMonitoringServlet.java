package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.ComponentStatus;
import model.VehicleManagement.Vehicle;
import data.VehicleDAO;
import data.ComponentStatusDAO;
import data.DatabaseConnection;
import model.MaintenanceTask.MaintenanceTaskManager;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.time.LocalDateTime;

@WebServlet(name = "ComponentMonitoringServlet", urlPatterns = {"/maintenance", "/getLastMaintenance", "/getComponentStatus", "/updateComponentStatus"})
public class ComponentMonitoringServlet extends HttpServlet {
    private Connection connection;
    private VehicleDAO vehicleDAO;
    private ComponentStatusDAO componentStatusDAO;

    @Override
    public void init() throws ServletException {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            vehicleDAO = new VehicleDAO(connection);
            componentStatusDAO = new ComponentStatusDAO(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ComponentMonitoringServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        
        switch (pathInfo) {
            case "/getLastMaintenance":
                handleGetLastMaintenance(request, response);
                break;
            case "/getComponentStatus":
                handleGetComponentStatus(request, response);
                break;
            case "/maintenance":
                handleMaintenancePage(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        
        if ("/updateComponentStatus".equals(pathInfo)) {
            handleUpdateComponentStatus(request, response);
        } else if ("/maintenance".equals(pathInfo)) {
            handleMaintenanceCheck(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleMaintenancePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            request.setAttribute("vehicleList", vehicles);
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleMaintenanceCheck(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            String[] components = request.getParameterValues("components");
            
            System.out.println("Vehicle Number: " + vehicleNumber);
            System.out.println("Selected Components: " + (components != null ? String.join(", ", components) : "none"));
            
            if (vehicleNumber == null || vehicleNumber.isEmpty()) {
                request.setAttribute("error", "請選擇車輛號碼");
                handleMaintenancePage(request, response);
                return;
            }

            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            Vehicle vehicle = vehicles.stream()
                .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                .findFirst()
                .orElse(null);

            if (vehicle != null) {
                // 更新最後維修日期
                java.util.Date utilDate = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                vehicle.setLastMaintenanceDate(sqlDate);
                vehicleDAO.updateVehicle(vehicle);
                
                // 更新組件狀態
                if (components != null && components.length > 0) {
                    for (String component : components) {
                        ComponentStatus status = new ComponentStatus(
                            vehicleNumber,
                            component.toLowerCase(),
                            0,
                            0.0
                        );
                        status.setLastUpdated(LocalDateTime.now());
                        status.setStatus("NORMAL");
                        
                        System.out.println("Updating component: " + component);
                        componentStatusDAO.updateComponentStatus(status);
                    }
                }
                
                // 獲取所有組件的最新狀態
                List<ComponentStatus> allStatuses = componentStatusDAO.getComponentStatusesByVehicle(vehicleNumber);
                System.out.println("Retrieved statuses count: " + allStatuses.size());
                
                // 設置屬性以便在JSP中顯示
                request.setAttribute("componentStatuses", allStatuses);
                request.setAttribute("currentVehicle", vehicle);
            }
            
            // 重新加載頁面
            handleMaintenancePage(request, response);
        } catch (Exception e) {
            System.err.println("Error in handleMaintenanceCheck: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleGetLastMaintenance(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            Vehicle vehicle = vehicles.stream()
                .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                .findFirst()
                .orElse(null);
            
            Map<String, Object> result = new HashMap<>();
            if (vehicle != null && vehicle.getLastMaintenanceDate() != null) {
                result.put("lastMaintenanceDate", vehicle.getLastMaintenanceDate().toString());
            } else {
                result.put("lastMaintenanceDate", "No maintenance record");
            }
            
            sendJsonResponse(response, result);
        } catch (Exception e) {
            System.err.println("Error in handleGetLastMaintenance: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleGetComponentStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            String sql = "SELECT cs.*, v.LastMaintenanceDate " +
                        "FROM Component_Status cs " +
                        "JOIN Vehicles v ON cs.VehicleID = v.VehicleNumber " +
                        "WHERE cs.VehicleID = ?";
            List<Map<String, Object>> statuses = new ArrayList<>();
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, vehicleNumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> status = new HashMap<>();
                        status.put("vehicleNumber", vehicleNumber);
                        status.put("componentName", rs.getString("ComponentName"));
                        
                        // 計算使用時間
                        Date lastMaintenanceDate = rs.getDate("LastMaintenanceDate");
                        long hoursUsed = 0;
                        if (lastMaintenanceDate != null) {
                            hoursUsed = (System.currentTimeMillis() - lastMaintenanceDate.getTime()) / (60 * 60 * 1000);
                        }
                        status.put("hoursUsed", hoursUsed);
                        
                        // 計算磨損程度
                        double wearLevel = calculateWearLevel(hoursUsed);
                        status.put("wearLevel", wearLevel);
                        
                        // 設置狀態
                        String componentStatus = "NORMAL";
                        if (wearLevel >= 80) {
                            componentStatus = "CRITICAL";
                        } else if (wearLevel >= 60) {
                            componentStatus = "WARNING";
                        }
                        status.put("status", componentStatus);
                        
                        status.put("lastUpdated", rs.getTimestamp("LastUpdated"));
                        statuses.add(status);
                    }
                }
            }
            
            sendJsonResponse(response, statuses);
        } catch (Exception e) {
            System.err.println("Error in handleGetComponentStatus: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private double calculateWearLevel(long hoursUsed) {
        if (hoursUsed >= 300) {
            return 80;
        } else if (hoursUsed >= 200) {
            return 60;
        } else if (hoursUsed >= 100) {
            return 40;
        } else {
            return (hoursUsed / 100.0) * 40;
        }
    }

    private void handleUpdateComponentStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // 讀取請求體
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            // 解析 JSON 數據
            JSONObject jsonData = new JSONObject(jsonBuilder.toString());
            String vehicleNumber = jsonData.getString("vehicleNumber");
            String lastMaintenanceDate = jsonData.getString("lastMaintenanceDate");
            int hoursUsed = jsonData.getInt("hoursUsed");

            // 更新機械組件狀態
            JSONArray mechanicalComponents = jsonData.getJSONArray("mechanicalComponents");
            for (int i = 0; i < mechanicalComponents.length(); i++) {
                JSONObject component = mechanicalComponents.getJSONObject(i);
                updateComponentStatus(vehicleNumber, "mechanical", component.getString("component"), 
                                   hoursUsed, component.getInt("wearLevel"), null, lastMaintenanceDate);
            }

            // 更新電氣組件狀態
            JSONArray electricalComponents = jsonData.getJSONArray("electricalComponents");
            for (int i = 0; i < electricalComponents.length(); i++) {
                JSONObject component = electricalComponents.getJSONObject(i);
                updateComponentStatus(vehicleNumber, "electrical", component.getString("component"), 
                                   hoursUsed, component.getInt("wearLevel"), null, lastMaintenanceDate);
            }

            // 更新引擎狀態
            JSONArray engineParameters = jsonData.getJSONArray("engineParameters");
            for (int i = 0; i < engineParameters.length(); i++) {
                JSONObject parameter = engineParameters.getJSONObject(i);
                updateComponentStatus(vehicleNumber, "engine", parameter.getString("parameter"), 
                                   null, null, parameter.getString("status"), lastMaintenanceDate);
            }

            // 發送成功響應
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"success\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private void updateComponentStatus(String vehicleNumber, String componentType, String componentName, 
                                     Integer hoursUsed, Integer wearLevel, String status, 
                                     String lastMaintenanceDate) throws SQLException {
        // 先刪除舊的記錄
        String deleteSql = "DELETE FROM Component_Status WHERE VehicleID = ? AND ComponentType = ? AND ComponentName = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, vehicleNumber);
            deleteStmt.setString(2, componentType);
            deleteStmt.setString(3, componentName);
            deleteStmt.executeUpdate();
        }

        // 插入新的記錄
        String insertSql = "INSERT INTO Component_Status (VehicleID, ComponentType, ComponentName, " +
                         "HoursUsed, WearLevel, Status, LastMaintenanceDate) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, vehicleNumber);
            insertStmt.setString(2, componentType);
            insertStmt.setString(3, componentName);
            if (hoursUsed != null) {
                insertStmt.setInt(4, hoursUsed);
            } else {
                insertStmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (wearLevel != null) {
                insertStmt.setInt(5, wearLevel);
            } else {
                insertStmt.setNull(5, java.sql.Types.INTEGER);
            }
            insertStmt.setString(6, status);
            insertStmt.setString(7, lastMaintenanceDate);
            insertStmt.executeUpdate();
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject(data);
        out.print(jsonObject.toString());
        out.flush();
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 