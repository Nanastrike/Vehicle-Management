package model.MaintenanceTask;

import data.MaintenanceTasksDAO;
import data.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceTaskManager {
    private final MaintenanceTasksDAO taskDAO;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenanceTaskManager() throws SQLException {
        this.taskDAO = new MaintenanceTasksDAO(DatabaseConnection.getInstance().getConnection());
        this.componentMonitor = new VehicleComponentMonitor();
    }
    
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskDAO.getPendingTasks();
    }
    
    public void createMaintenanceTask(int vehicleId, String taskType, LocalDateTime scheduledDate, String createdBy, String priority) throws SQLException {
        MaintenanceTask task = new MaintenanceTask();
        task.setVehicleId(String.valueOf(vehicleId));
        task.setTaskType(taskType);
        task.setScheduledDate(scheduledDate);
        task.setCreatedBy(createdBy);
        task.setStatus("Scheduled");
        task.setPriority(priority);
        
        taskDAO.createTask(task);
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskDAO.updateTaskStatus(taskId, status);
    }
    
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskDAO.deleteTask(taskId);
    }
    
    public void monitorComponents(String vehicleId, String componentName, int hoursUsed, double wearLevel) {
        ComponentStatus status = new ComponentStatus(vehicleId, componentName, hoursUsed, wearLevel);
        componentMonitor.updateComponentStatus(status);
    }
} 