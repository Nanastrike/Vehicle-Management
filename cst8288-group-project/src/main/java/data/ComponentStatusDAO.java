package data;

import model.MaintenanceTask.ComponentStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for handling component status database operations.
 * Provides methods for creating, updating, and querying component status records
 * in the database.
 *
 * <p>This class manages the persistence of component status information,
 * including wear levels, hours used, and current operational status.</p>
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentStatus
 */
public class ComponentStatusDAO {
    /** Database connection instance */
    private final Connection connection;
    
    /**
     * Constructs a new ComponentStatusDAO with the specified database connection.
     * @param connection The database connection to be used for operations
     */
    public ComponentStatusDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a new component status record in the database.
     * @param status The component status object to be created
     * @throws SQLException if a database access error occurs
     */
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
    
    /**
     * Retrieves all component statuses for a specific vehicle.
     * @param vehicleId The ID of the vehicle to get component statuses for
     * @return List of component status records associated with the vehicle
     * @throws SQLException if a database access error occurs
     */
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
    
    /**
     * Updates an existing component status or creates a new one if it doesn't exist.
     * Performs a check for existing component before deciding whether to update or create.
     * @param status The component status object to be updated or created
     * @throws SQLException if a database access error occurs
     */
    public void updateComponentStatus(ComponentStatus status) throws SQLException {
        // Check if component exists
        String checkSql = "SELECT ComponentID FROM Component_Status WHERE VehicleID = ? AND ComponentName = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, status.getVehicleId());
            checkStmt.setString(2, status.getComponentName());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Component exists, update it
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
                    // Component doesn't exist, create it
                    createComponentStatus(status);
                }
            }
        }
    }
    
    /**
     * Retrieves a specific component status by its ID.
     * @param componentId The ID of the component to retrieve
     * @return The component status object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
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
    
    /**
     * Performs a batch update of multiple component statuses.
     * This method is more efficient than updating components individually.
     * @param statuses List of component status objects to be updated
     * @throws SQLException if a database access error occurs during batch execution
     */
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