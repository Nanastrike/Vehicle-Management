package presentation;

import businesslayer.MaintenanceTaskController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/maintenance/*")
public class MaintenanceServlet extends HttpServlet {
    private final MaintenanceTaskController taskController;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenanceServlet() throws ServletException {
        try {
            this.taskController = new MaintenanceTaskController();
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
                // Get all maintenance tasks
                List<MaintenanceTask> tasks = taskController.getAllMaintenanceTasks();
                sendJsonResponse(response, tasks);
            } else if (pathInfo.startsWith("/vehicle/")) {
                // Get maintenance tasks for a specific vehicle
                String vehicleId = pathInfo.substring(9);
                List<MaintenanceTask> tasks = taskController.getTasksByVehicleId(vehicleId);
                sendJsonResponse(response, tasks);
            } else if (pathInfo.startsWith("/pending")) {
                // Get pending maintenance tasks
                List<MaintenanceTask> tasks = taskController.getPendingTasks();
                sendJsonResponse(response, tasks);
            } else if (pathInfo.startsWith("/alerts")) {
                // Get component alerts
                List<ComponentStatus> statuses = componentMonitor.getComponentStatuses();
                List<MaintenanceAlert> alerts = statuses.stream()
                    .map(MaintenanceAlert::new)
                    .collect(Collectors.toList());
                sendJsonResponse(response, alerts);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Read JSON data from request body
            String jsonBody = request.getReader().lines().collect(Collectors.joining());
            
            // Parse JSON data
            MaintenanceTask task = parseJsonToTask(jsonBody);
            
            // Create maintenance task
            taskController.createMaintenanceTask(
                task.getVehicleId(),
                task.getComponentType(),
                task.getTaskDescription(),
                task.getScheduledDate(),
                task.getCreatedBy()
            );
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            sendJsonResponse(response, task);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || !pathInfo.startsWith("/status/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            return;
        }
        
        try {
            // Parse task ID and status
            String[] parts = pathInfo.substring(7).split("/");
            if (parts.length != 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format");
                return;
            }
            
            int taskId = Integer.parseInt(parts[0]);
            String status = parts[1];
            
            // Update task status
            taskController.updateTaskStatus(taskId, status);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Status updated successfully\"}");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(pathInfo.substring(1));
            taskController.deleteMaintenanceTask(taskId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\":\"Task deleted successfully\"}");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertToJson(data));
    }
    
    private String convertToJson(Object obj) {
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            return "[" + list.stream()
                    .map(this::convertToJson)
                    .collect(Collectors.joining(",")) + "]";
        } else if (obj instanceof MaintenanceTask) {
            MaintenanceTask task = (MaintenanceTask) obj;
            return String.format(
                "{\"taskId\":%d,\"vehicleId\":\"%s\",\"componentType\":\"%s\"," +
                "\"taskDescription\":\"%s\",\"scheduledDate\":\"%s\",\"status\":\"%s\"," +
                "\"createdBy\":\"%s\",\"createdAt\":\"%s\"}",
                task.getTaskId(),
                task.getVehicleId(),
                task.getComponentType(),
                task.getTaskDescription(),
                task.getScheduledDate(),
                task.getStatus(),
                task.getCreatedBy(),
                task.getCreatedAt()
            );
        } else if (obj instanceof MaintenanceAlert) {
            MaintenanceAlert alert = (MaintenanceAlert) obj;
            ComponentStatus status = alert.getComponentStatus();
            return String.format(
                "{\"timestamp\":\"%s\",\"vehicleId\":\"%s\",\"componentType\":\"%s\"," +
                "\"wearLevel\":%.1f,\"status\":\"%s\",\"alertMessage\":\"%s\"}",
                alert.getTimestamp(),
                status.getVehicleId(),
                status.getComponentType(),
                status.getWearLevel(),
                status.getStatus(),
                status.getAlertMessage()
            );
        }
        return "null";
    }
    
    private MaintenanceTask parseJsonToTask(String json) {
        // Simple JSON parsing
        json = json.replaceAll("[{}\"]", "");
        String[] pairs = json.split(",");
        String vehicleId = null;
        String componentType = null;
        String taskDescription = null;
        LocalDateTime scheduledDate = null;
        String createdBy = null;
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            
            switch (key) {
                case "vehicleId":
                    vehicleId = value;
                    break;
                case "componentType":
                    componentType = value;
                    break;
                case "taskDescription":
                    taskDescription = value;
                    break;
                case "scheduledDate":
                    scheduledDate = LocalDateTime.parse(value);
                    break;
                case "createdBy":
                    createdBy = value;
                    break;
            }
        }
        
        if (vehicleId == null || componentType == null || taskDescription == null || 
            scheduledDate == null || createdBy == null) {
            throw new IllegalArgumentException("Missing required fields in JSON");
        }
        
        return new MaintenanceTask(vehicleId, componentType, taskDescription, scheduledDate, createdBy);
    }
} 