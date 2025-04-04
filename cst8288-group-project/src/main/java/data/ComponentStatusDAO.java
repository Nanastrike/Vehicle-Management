package data;

import model.MaintenanceTask.ComponentStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComponentStatusDAO {
    private final Connection connection;
    
    public ComponentStatusDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void createComponentStatus(ComponentStatus status) throws SQLException {
        String sql = "INSERT INTO Component_Status (VehicleID, ComponentName, HoursUsed, WearLevel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, status.getVehicleId());
            stmt.setString(2, status.getComponentName());
            stmt.setInt(3, status.getHoursUsed());
            stmt.setDouble(4, status.getWearLevel());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    status.setComponentId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<ComponentStatus> getComponentStatusesByVehicle(String vehicleId) throws SQLException {
        String sql = "SELECT * FROM Component_Status WHERE VehicleID = ?";
        List<ComponentStatus> statuses = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ComponentStatus status = new ComponentStatus(
                        rs.getString("VehicleID"),
                        rs.getString("ComponentName"),
                        rs.getInt("HoursUsed"),
                        rs.getDouble("WearLevel")
                    );
                    status.setComponentId(rs.getInt("ComponentID"));
                    status.setLastUpdated(rs.getTimestamp("LastUpdated").toLocalDateTime());
                    statuses.add(status);
                }
            }
        }
        return statuses;
    }
    
    public void updateComponentStatus(ComponentStatus status) throws SQLException {
        String sql = "UPDATE Component_Status SET HoursUsed = ?, WearLevel = ?, LastUpdated = ? WHERE ComponentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status.getHoursUsed());
            stmt.setDouble(2, status.getWearLevel());
            stmt.setTimestamp(3, Timestamp.valueOf(status.getLastUpdated()));
            stmt.setInt(4, status.getComponentId());
            stmt.executeUpdate();
        }
    }
    
    public ComponentStatus getComponentStatus(int componentId) throws SQLException {
        String sql = "SELECT * FROM Component_Status WHERE ComponentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, componentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ComponentStatus status = new ComponentStatus(
                        rs.getString("VehicleID"),
                        rs.getString("ComponentName"),
                        rs.getInt("HoursUsed"),
                        rs.getDouble("WearLevel")
                    );
                    status.setComponentId(rs.getInt("ComponentID"));
                    status.setLastUpdated(rs.getTimestamp("LastUpdated").toLocalDateTime());
                    return status;
                }
            }
        }
        return null;
    }
} 