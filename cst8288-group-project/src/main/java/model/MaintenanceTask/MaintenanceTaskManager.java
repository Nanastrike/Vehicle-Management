package model.MaintenanceTask;

import data.MaintenanceTaskDAO;
import data.ComponentStatusDAO;
import data.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

/**
 * Manages maintenance tasks and vehicle component monitoring.
 * Provides operations such as creating, updating, deleting tasks, and tracking component status.
 * 
 * @author Yen-Yi Hsu
 */
public class MaintenanceTaskManager {
    private final MaintenanceTaskDAO taskDAO;
    private final ComponentStatusDAO componentStatusDAO;
    private final VehicleComponentMonitor componentMonitor;

    /**
     * Initializes the DAO and monitoring components.
     * 
     * @throws SQLException if a database connection fails
     */
    public MaintenanceTaskManager() throws SQLException {
        this.taskDAO = new MaintenanceTaskDAO(DatabaseConnection.getInstance().getConnection());
        this.componentStatusDAO = new ComponentStatusDAO(DatabaseConnection.getInstance().getConnection());
        this.componentMonitor = new VehicleComponentMonitor();
    }

    /**
     * Retrieves all pending maintenance tasks.
     * 
     * @return list of pending MaintenanceTask objects
     * @throws SQLException if a database error occurs
     */
    public List<MaintenanceTask> getAllMaintenanceTasks() throws SQLException {
        return taskDAO.getPendingTasks();
    }

    /**
     * Creates a new maintenance task for a vehicle.
     * 
     * @param vehicleId ID of the vehicle
     * @param taskType type of the maintenance task
     * @param scheduledDate date the task is scheduled
     * @param createdBy user who created the task
     * @param priority priority level of the task
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Updates the status of an existing maintenance task.
     * 
     * @param taskId ID of the task
     * @param status new status (e.g., "Completed", "In Progress")
     * @throws SQLException if a database error occurs
     */
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        taskDAO.updateTaskStatus(taskId, status);
    }

    /**
     * Deletes a maintenance task by its ID.
     * 
     * @param taskId ID of the task to delete
     * @throws SQLException if a database error occurs
     */
    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskDAO.deleteTask(taskId);
    }

    /**
     * Monitors vehicle components and stores their status based on usage since last maintenance.
     * 
     * @param vehicleId ID of the vehicle
     * @param components array of component names to monitor
     * @param lastMaintenanceDate date of the last maintenance
     * @throws SQLException if a database error occurs
     */
    public void monitorComponents(String vehicleId, String[] components, Date lastMaintenanceDate) throws SQLException {
        // Calculate hours used since last maintenance
        long hoursUsed = (System.currentTimeMillis() - lastMaintenanceDate.getTime()) / (60 * 60 * 1000);

        // Determine wear level based on usage
        double wearLevel = calculateWearLevel(hoursUsed);

        // Create and save status for each component
        for (String component : components) {
            ComponentStatus status = new ComponentStatus(
                vehicleId,
                component,
                (int) hoursUsed,
                wearLevel
            );

            componentStatusDAO.createComponentStatus(status);
            componentMonitor.updateComponentStatus(status);
        }
    }

    /**
     * Calculates the wear level of a component based on hours used.
     * 
     * @param hoursUsed hours since last maintenance
     * @return wear level as a percentage
     */
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

    /**
     * Retrieves component status records for a specific vehicle.
     * 
     * @param vehicleId ID of the vehicle
     * @return list of ComponentStatus objects
     * @throws SQLException if a database error occurs
     */
    public List<ComponentStatus> getComponentStatuses(String vehicleId) throws SQLException {
        return componentStatusDAO.getComponentStatusesByVehicle(vehicleId);
    }
}
