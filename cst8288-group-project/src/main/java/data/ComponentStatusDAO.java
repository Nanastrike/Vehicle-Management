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
        String sql = "INSERT INTO Component_Status (VehicleID, ComponentName, HoursUsed, WearLevel, Status, LastUpdated) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, status.getVehicleId());
            stmt.setString(2, status.getComponentName());
            stmt.setInt(3, status.getHoursUsed());
            stmt.setDouble(4, status.getWearLevel());
            stmt.setString(5, status.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(status.getLastUpdated()));
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
                    status.setStatus(rs.getString("Status"));
                    statuses.add(status);
                }
            }
        }
        return statuses;
    }
    
    public void updateComponentStatus(ComponentStatus status) throws SQLException {
        // 先檢查組件是否存在
        String checkSql = "SELECT ComponentID FROM Component_Status WHERE VehicleID = ? AND ComponentName = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, status.getVehicleId());
            checkStmt.setString(2, status.getComponentName());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // 組件存在，更新它
                    String updateSql = "UPDATE Component_Status SET HoursUsed = ?, WearLevel = ?, Status = ?, LastUpdated = ? WHERE ComponentID = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, status.getHoursUsed());
                        updateStmt.setDouble(2, status.getWearLevel());
                        updateStmt.setString(3, status.getStatus());
                        updateStmt.setTimestamp(4, Timestamp.valueOf(status.getLastUpdated()));
                        updateStmt.setInt(5, rs.getInt("ComponentID"));
                        updateStmt.executeUpdate();
                    }
                } else {
                    // 組件不存在，創建它
                    createComponentStatus(status);
                }
            }
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
                    status.setStatus(rs.getString("Status"));
                    return status;
                }
            }
        }
        return null;
    }
    
    public void batchUpdateComponentStatus(List<ComponentStatus> statuses) throws SQLException {
        String sql = "UPDATE Component_Status SET HoursUsed = ?, WearLevel = ?, Status = ?, LastUpdated = ? " +
                    "WHERE VehicleID = ? AND ComponentName = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ComponentStatus status : statuses) {
                stmt.setInt(1, status.getHoursUsed());
                stmt.setDouble(2, status.getWearLevel());
                stmt.setString(3, status.getStatus());
                stmt.setTimestamp(4, Timestamp.valueOf(status.getLastUpdated()));
                stmt.setString(5, status.getVehicleId());
                stmt.setString(6, status.getComponentName());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
} 