package data;

import model.MaintenanceTask.MaintenanceTask;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceTaskDAO {
    private final Connection connection;
    
    public MaintenanceTaskDAO(Connection connection) {
        this.connection = connection;
    }
    
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
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        String sql = "UPDATE Maintenance_Tasks SET Status = ? WHERE TaskID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            stmt.executeUpdate();
        }
    }
    
    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM Maintenance_Tasks WHERE TaskID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }
    
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
    
    // ✅ Count of HIGH priority maintenance tasks
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

    // ✅ Most recent maintenance task
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