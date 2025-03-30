package data;

import model.MaintenanceTask.MaintenanceTask;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceTaskDAO {
    private Connection conn;
    
    public MaintenanceTaskDAO(Connection conn) {
        this.conn = conn;
    }
    
    public void createTask(MaintenanceTask task) throws SQLException {
        String sql = "INSERT INTO maintenance_tasks (vehicle_id, component_type, task_description, " +
                    "scheduled_date, status, created_by, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getVehicleId());
            pstmt.setString(2, task.getComponentType());
            pstmt.setString(3, task.getTaskDescription());
            pstmt.setTimestamp(4, Timestamp.valueOf(task.getScheduledDate()));
            pstmt.setString(5, task.getStatus());
            pstmt.setString(6, task.getCreatedBy());
            pstmt.setTimestamp(7, Timestamp.valueOf(task.getCreatedAt()));
            
            pstmt.executeUpdate();
            System.out.println("Maintenance task created successfully");
        }
    }
    
    public List<MaintenanceTask> getAllTasks() throws SQLException {
        List<MaintenanceTask> tasks = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_tasks ORDER BY scheduled_date";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MaintenanceTask task = new MaintenanceTask(
                    rs.getString("vehicle_id"),
                    rs.getString("component_type"),
                    rs.getString("task_description"),
                    rs.getTimestamp("scheduled_date").toLocalDateTime(),
                    rs.getString("created_by")
                );
                task.setTaskId(rs.getInt("task_id"));
                task.setStatus(rs.getString("status"));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tasks.add(task);
            }
        }
        return tasks;
    }
    
    public void updateTaskStatus(int taskId, String status) throws SQLException {
        String sql = "UPDATE maintenance_tasks SET status = ? WHERE task_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, taskId);
            
            pstmt.executeUpdate();
            System.out.println("Task status updated successfully");
        }
    }
    
    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM maintenance_tasks WHERE task_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            
            pstmt.executeUpdate();
            System.out.println("Task deleted successfully");
        }
    }
} 