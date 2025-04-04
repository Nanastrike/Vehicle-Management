package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.VehicleManagement.Vehicle;
import model.MaintenanceTask.MaintenanceTask;

import java.util.*;

public class DashboardDAO {

    private VehicleDAO vehicleDAO;
    private Connection conn; // ✅ Add this field

    public DashboardDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
        this.conn = DatabaseConnection.getInstance().getConnection(); // ✅ Initialize it
    }

    // Get count of each vehicle type
    public Map<String, Integer> getVehicleTypeCounts() {
        Map<String, Integer> counts = new HashMap<>();
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicleList) {
            String type = v.getVehicleType().getTypeName();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        return counts;
    }

    // Get the last added vehicle
    public Vehicle getLastAddedVehicle() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        return vehicles.isEmpty() ? null : vehicles.get(vehicles.size() - 1);
    }

    // ✅ Count of HIGH priority maintenance tasks
    public int getHighPriorityTaskCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Maintenance_Tasks WHERE Priority = 'HIGH'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
