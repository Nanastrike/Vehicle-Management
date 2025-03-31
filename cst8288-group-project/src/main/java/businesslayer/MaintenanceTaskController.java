package businesslayer;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.MaintenanceTaskManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceTaskController {
    private final MaintenanceTaskManager taskManager;
    
    public MaintenanceTaskController() throws SQLException {
        this.taskManager = new MaintenanceTaskManager();
    }
    
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskManager.getAllMaintenanceTasks();
    }
    
    public void createMaintenanceTask(String vehicleId, String componentType, 
            String description, LocalDateTime scheduledDate, String createdBy) throws SQLException {
        taskManager.createMaintenanceTask(vehicleId, componentType, description, scheduledDate, createdBy);
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskManager.updateTaskStatus(taskId, status);
    }
    
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskManager.deleteMaintenanceTask(taskId);
    }
    
    public void monitorComponents(String vehicleId, String componentType, 
            double wearLevel, String alertMessage) {
        taskManager.monitorComponents(vehicleId, componentType, wearLevel, alertMessage);
    }
} 