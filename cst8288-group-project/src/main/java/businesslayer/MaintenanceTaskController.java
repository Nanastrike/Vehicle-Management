package businesslayer;

import data.MaintenanceTaskDAO;
import data.DatabaseConnection;
import model.MaintenanceTask.MaintenanceTask;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceTaskController {
    private final MaintenanceTaskDAO taskDAO;
    
    public MaintenanceTaskController() throws SQLException {
        this.taskDAO = new MaintenanceTaskDAO(DatabaseConnection.getInstance().getConnection());
    }
    
    public void createMaintenanceTask(String vehicleId, String componentType, 
                                    String description, LocalDateTime scheduledDate, 
                                    String createdBy) throws SQLException {
        MaintenanceTask task = new MaintenanceTask(vehicleId, componentType, 
                                                 description, scheduledDate, createdBy);
        taskDAO.createTask(task);
    }
    
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskDAO.getAllTasks();
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskDAO.updateTaskStatus(taskId, status);
    }
    
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskDAO.deleteTask(taskId);
    }
    
    public List<MaintenanceTask> getTasksByVehicleId(String vehicleId) throws SQLException {
        return taskDAO.getAllTasks().stream()
                     .filter(task -> task.getVehicleId().equals(vehicleId))
                     .toList();
    }
    
    public List<MaintenanceTask> getPendingTasks() throws SQLException {
        return taskDAO.getAllTasks().stream()
                     .filter(task -> task.getStatus().equals("PENDING"))
                     .toList();
    }
} 