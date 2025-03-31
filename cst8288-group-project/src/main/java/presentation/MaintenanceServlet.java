package presentation;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenanceTaskManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/maintenance/*")
public class MaintenanceServlet extends HttpServlet {
    private final MaintenanceTaskManager taskManager;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenanceServlet() throws ServletException {
        try {
            this.taskManager = new MaintenanceTaskManager();
            this.componentMonitor = new VehicleComponentMonitor();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize MaintenanceServlet", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // 獲取所有維護任務
                List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
                response.setContentType("application/json");
                response.getWriter().write(tasks.toString());
            } else if (pathInfo.equals("/alerts")) {
                // 獲取所有警報
                List<MaintenanceAlert> alerts = componentMonitor.getComponentStatuses().stream()
                    .map(MaintenanceAlert::new)
                    .collect(Collectors.toList());
                response.setContentType("application/json");
                response.getWriter().write(alerts.toString());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // 創建新的維護任務
                String vehicleId = request.getParameter("vehicleId");
                String componentType = request.getParameter("componentType");
                String description = request.getParameter("description");
                String scheduledDateStr = request.getParameter("scheduledDate");
                String createdBy = request.getParameter("createdBy");
                
                LocalDateTime scheduledDate = LocalDateTime.parse(scheduledDateStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                
                taskManager.createMaintenanceTask(vehicleId, componentType, description, 
                    scheduledDate, createdBy);
                
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else if (pathInfo.equals("/mechanical")) {
                // 監控機械組件
                String vehicleId = request.getParameter("vehicleId");
                double brakeWear = Double.parseDouble(request.getParameter("brakeWear"));
                double wheelWear = Double.parseDouble(request.getParameter("wheelWear"));
                double bearingWear = Double.parseDouble(request.getParameter("bearingWear"));
                
                componentMonitor.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (pathInfo.equals("/electrical")) {
                // 監控電氣組件
                String vehicleId = request.getParameter("vehicleId");
                double catenaryWear = Double.parseDouble(request.getParameter("catenaryWear"));
                double pantographWear = Double.parseDouble(request.getParameter("pantographWear"));
                double breakerWear = Double.parseDouble(request.getParameter("breakerWear"));
                
                componentMonitor.monitorElectricalComponents(vehicleId, catenaryWear, 
                    pantographWear, breakerWear);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (pathInfo.equals("/engine")) {
                // 監控引擎診斷
                String vehicleId = request.getParameter("vehicleId");
                double engineTemp = Double.parseDouble(request.getParameter("engineTemp"));
                double oilPressure = Double.parseDouble(request.getParameter("oilPressure"));
                double fuelEfficiency = Double.parseDouble(request.getParameter("fuelEfficiency"));
                
                componentMonitor.monitorEngineDiagnostics(vehicleId, engineTemp, 
                    oilPressure, fuelEfficiency);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (pathInfo.equals("/clear")) {
                // 清除所有警報
                componentMonitor.clearAlerts();
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 