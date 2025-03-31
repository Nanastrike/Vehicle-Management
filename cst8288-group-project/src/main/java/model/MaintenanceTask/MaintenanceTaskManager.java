package model.MaintenanceTask;

import data.MaintenanceTaskDAO;
import data.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceTaskManager {
    private final MaintenanceTaskDAO taskDAO;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenanceTaskManager() throws SQLException {
        this.taskDAO = new MaintenanceTaskDAO(DatabaseConnection.getInstance().getConnection());
        this.componentMonitor = new VehicleComponentMonitor();
    }
    
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskDAO.getAllTasks();
    }
    
    public void createMaintenanceTask(String vehicleId, String componentType, 
            String description, LocalDateTime scheduledDate, String createdBy) throws SQLException {
        MaintenanceTask task = new MaintenanceTask(vehicleId, componentType, description, scheduledDate, createdBy);
        taskDAO.createTask(task);
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskDAO.updateTaskStatus(taskId, status);
    }
    
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskDAO.deleteTask(taskId);
    }
    
    public void monitorComponents(String vehicleId, String componentType, 
            double wearLevel, String alertMessage) {
        ComponentStatus status = new ComponentStatus(
            vehicleId, componentType, wearLevel, 
            wearLevel > 80 ? "CRITICAL" : (wearLevel > 60 ? "WARNING" : "NORMAL"),
            alertMessage
        );
        componentMonitor.updateComponentStatus(status);
    }
} 