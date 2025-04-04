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
import data.DatabaseConnection;
import model.MaintenanceTask.MaintenanceTaskManager;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Date;

@WebServlet(name = "ComponentMonitoringServlet", urlPatterns = {"/maintenance", "/getLastMaintenance", "/getComponentStatus"})
public class ComponentMonitoringServlet extends HttpServlet {
    private VehicleComponentMonitor componentMonitor;
    private VehicleDAO vehicleDAO;

    @Override
    public void init() throws ServletException {
        try {
            this.componentMonitor = new VehicleComponentMonitor();
            this.vehicleDAO = new VehicleDAO(DatabaseConnection.getInstance().getConnection());
            System.out.println("ComponentMonitoringServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing ComponentMonitoringServlet: " + e.getMessage());
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
                handleMaintenanceCheck(request, response);
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
        
        if ("/maintenance".equals(pathInfo)) {
            handleMaintenanceCheck(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            Vehicle vehicle = vehicles.stream()
                .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                .findFirst()
                .orElse(null);
            
            Map<String, Object> result = new HashMap<>();
            if (vehicle != null) {
                // 獲取組件使用時間（從最後維修日期到現在的小時數）
                Date lastMaintenance = vehicle.getLastMaintenanceDate();
                if (lastMaintenance != null) {
                    long hoursUsed = (System.currentTimeMillis() - lastMaintenance.getTime()) / (60 * 60 * 1000);
                    
                    // 計算剎車磨損程度
                    double brakeWearLevel = 0;
                    if (hoursUsed >= 300) {
                        brakeWearLevel = 80;
                    } else if (hoursUsed >= 200) {
                        brakeWearLevel = 60;
                    } else if (hoursUsed >= 100) {
                        brakeWearLevel = 40;
                    } else {
                        brakeWearLevel = (hoursUsed / 100.0) * 40;
                    }
                    
                    result.put("vehicleNumber", vehicleNumber);
                    result.put("brakeHours", hoursUsed);
                    result.put("brakeWearLevel", brakeWearLevel);
                } else {
                    result.put("vehicleNumber", vehicleNumber);
                    result.put("brakeHours", 0);
                    result.put("brakeWearLevel", 0);
                }
            } else {
                result.put("error", "Vehicle not found");
            }
            
            sendJsonResponse(response, result);
        } catch (Exception e) {
            System.err.println("Error in handleGetComponentStatus: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleMaintenanceCheck(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            String[] components = request.getParameterValues("components");
            
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            Vehicle vehicle = vehicles.stream()
                .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                .findFirst()
                .orElse(null);

            if (vehicle != null) {
                // 更新車輛的最後維修日期
                java.util.Date utilDate = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                vehicle.setLastMaintenanceDate(sqlDate);
                vehicleDAO.updateVehicle(vehicle);
                
                // 使用MaintenanceTaskManager處理組件監控
                MaintenanceTaskManager taskManager = new MaintenanceTaskManager();
                taskManager.monitorComponents(vehicleNumber, components, vehicle.getLastMaintenanceDate());
                
                // 獲取更新後的組件狀態
                List<ComponentStatus> statuses = taskManager.getComponentStatuses(vehicleNumber);
                request.setAttribute("componentStatuses", statuses);
                
                // 轉發到JSP頁面
                request.getRequestDispatcher("maintenance.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vehicle not found");
            }
        } catch (Exception e) {
            System.err.println("Error in handleMaintenanceCheck: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
} 