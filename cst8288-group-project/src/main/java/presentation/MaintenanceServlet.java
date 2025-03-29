package presentation;

import businesslayer.MaintenanceTaskController;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/api/maintenance/*")
public class MaintenanceServlet extends HttpServlet {
    private final MaintenanceTaskController taskController;
    private final Gson gson;
    
    public MaintenanceServlet() {
        this.taskController = new MaintenanceTaskController();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // 獲取所有維護任務
                List<MaintenanceTask> tasks = taskController.getAllMaintenanceTasks();
                sendJsonResponse(response, tasks);
            } else if (pathInfo.startsWith("/vehicle/")) {
                // 獲取特定車輛的維護任務
                String vehicleId = pathInfo.substring(9);
                List<MaintenanceTask> tasks = taskController.getTasksByVehicleId(vehicleId);
                sendJsonResponse(response, tasks);
            } else if (pathInfo.startsWith("/pending")) {
                // 獲取待處理的維護任務
                List<MaintenanceTask> tasks = taskController.getPendingTasks();
                sendJsonResponse(response, tasks);
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
            // 從請求體中讀取 JSON 數據
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                buffer.append(line);
            }
            
            // 解析 JSON 數據
            MaintenanceTask task = gson.fromJson(buffer.toString(), MaintenanceTask.class);
            
            // 創建維護任務
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
            // 解析任務 ID 和狀態
            String[] parts = pathInfo.substring(7).split("/");
            if (parts.length != 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format");
                return;
            }
            
            int taskId = Integer.parseInt(parts[0]);
            String status = parts[1];
            
            // 更新任務狀態
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
        response.getWriter().write(gson.toJson(data));
    }
} 