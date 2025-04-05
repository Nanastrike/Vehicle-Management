package data;

import model.MaintenanceTask.MaintenanceTask;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for handling maintenance task database operations.
 * Provides methods for creating, updating, deleting, and querying maintenance tasks
 * in the database.
 *
 * This class handles all database interactions related to maintenance tasks,
 * including CRUD operations and specialized queries for task management.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see MaintenanceTask
 */
public class MaintenanceTaskDAO {
    /** Database connection instance */
    private final Connection connection;
    
    /**
     * Constructs a new MaintenanceTaskDAO with the specified database connection.
     * @param connection The database connection to be used for operations
     */
    public MaintenanceTaskDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a new maintenance task in the database.
     * @param task The maintenance task object to be created
     * @throws SQLException if a database access error occurs
     */
    public void createTask(MaintenanceTask task) throws SQLException {
        String sql = "INSERT INTO Maintenance_Tasks (VehicleID, MaintenanceDate, Status, CreatedBy, Priority, TaskType) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getVehicleId());
            stmt.setTimestamp(2, Timestamp.valueOf(task.getScheduledDate()));
            stmt.setString(3, task.getStatus());
            stmt.setString(4, task.getCreatedBy());
            stmt.setString(5, task.getPriority());
            stmt.setString(6, task.getTaskType());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setTaskId(rs.getInt(1));
                }
            }
        }
    }
    
    /**
     * Updates the status of a specific maintenance task.
     * @param taskId The ID of the maintenance task to update
     * @param status The new status to set
     * @throws SQLException if a database access error occurs
     */
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        String sql = "UPDATE Maintenance_Tasks SET Status = ? WHERE TaskID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Deletes a maintenance task from the database.
     * @param taskId The ID of the maintenance task to delete
     * @throws SQLException if a database access error occurs
     */
    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM Maintenance_Tasks WHERE TaskID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves all maintenance tasks for a specific vehicle.
     * @param vehicleId The ID of the vehicle to get tasks for
     * @return List of maintenance tasks associated with the vehicle
     * @throws SQLException if a database access error occurs
     */
    public List<MaintenanceTask> getTasksByVehicle(String vehicleId) throws SQLException {
        String sql = "SELECT * FROM Maintenance_Tasks WHERE VehicleID = ? ORDER BY MaintenanceDate";
        List<MaintenanceTask> tasks = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaintenanceTask task = new MaintenanceTask();
                    task.setTaskId(rs.getInt("TaskID"));
                    task.setVehicleId(rs.getString("VehicleID"));
                    task.setComponentId(rs.getInt("ComponentID"));
                    task.setScheduledDate(rs.getTimestamp("MaintenanceDate").toLocalDateTime());
                    task.setStatus(rs.getString("Status"));
                    task.setCreatedBy(rs.getString("CreatedBy"));
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }
    
    /**
     * Retrieves all pending (scheduled) maintenance tasks.
     * Tasks are joined with vehicle information and ordered by maintenance date.
     * @return List of pending maintenance tasks
     * @throws SQLException if a database access error occurs
     */
    public List<MaintenanceTask> getPendingTasks() throws SQLException {
        String sql = "SELECT mt.*, v.VehicleNumber FROM Maintenance_Tasks mt " +
                    "JOIN Vehicles v ON mt.VehicleID = v.VehicleID " +
                    "WHERE mt.Status = 'Scheduled' " +
                    "ORDER BY mt.MaintenanceDate";
        List<MaintenanceTask> tasks = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MaintenanceTask task = new MaintenanceTask();
                task.setTaskId(rs.getInt("TaskID"));
                task.setVehicleId(rs.getString("VehicleID"));
                task.setVehicleNumber(rs.getString("VehicleNumber"));
                task.setComponentId(rs.getInt("ComponentID"));
                task.setTaskType(rs.getString("TaskType"));
                task.setScheduledDate(rs.getTimestamp("MaintenanceDate").toLocalDateTime());
                task.setStatus(rs.getString("Status"));
                task.setCreatedBy(rs.getString("CreatedBy"));
                task.setPriority(rs.getString("Priority"));
                tasks.add(task);
            }
        }
        return tasks;
    }
    
    /**
     * Gets the count of high priority maintenance tasks.
     * @return The number of high priority tasks
     * @throws SQLException if a database access error occurs
     */
    public int getHighPriorityTaskCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Maintenance_Tasks WHERE Priority = 'High'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves the most recent maintenance task based on maintenance date.
     * @return The most recent maintenance task, or null if no tasks exist
     * @throws SQLException if a database access error occurs
     */
    public MaintenanceTask getMostRecentTask() throws SQLException {
        String sql = "SELECT * FROM Maintenance_Tasks ORDER BY MaintenanceDate DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                MaintenanceTask task = new MaintenanceTask();
                task.setTaskId(rs.getInt("TaskID"));
                task.setVehicleId(rs.getString("VehicleID"));
                task.setTaskType(rs.getString("TaskType"));
                task.setStatus(rs.getString("Status"));
                task.setPriority(rs.getString("Priority"));
                task.setScheduledDate(rs.getTimestamp("MaintenanceDate").toLocalDateTime());
                task.setCreatedBy(rs.getString("CreatedBy"));
                return task;
            }
        }
        return null;
    }
} 