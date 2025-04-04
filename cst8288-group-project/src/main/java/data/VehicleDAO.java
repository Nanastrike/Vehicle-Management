package data;

import model.VehicleManagement.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * VehicleDAO handles database operations for the Vehicles table.
 * Provides CRUD functionalities and supports vehicle lookup by ID or number.
 *
 * <p>This class uses JDBC to interact with the database and maps results to the {@link Vehicle} model.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 *
 * @see model.VehicleManagement.Vehicle
 * @see model.VehicleManagement.VehicleType
 * @see model.VehicleManagement.FuelType
 */
public class VehicleDAO {

    private Connection conn;

    /**
     * Constructor to accept a database connection.
     * @param conn the database connection instance
     */
    public VehicleDAO(Connection conn) {
        this.conn = conn;
    }


    public VehicleDAO() {
    this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // Add Vehicle

    /**
     * Adds a new vehicle to the database.
     * @param vehicle the vehicle object to be added
     * @return true if insertion is successful, false otherwise
     */
     public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicles (VehicleNumber, VehicleTypeID, FuelTypeID, ConsumptionRate, MaxPassengers, RouteID, LastMaintenanceDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getVehicleNumber());
            stmt.setInt(2, vehicle.getVehicleType().getVehicleTypeID());
            stmt.setInt(3, vehicle.getFuelType().getFuelTypeID());
            stmt.setFloat(4, vehicle.getConsumptionRate());
            stmt.setInt(5, vehicle.getMaxPassengers());
            stmt.setInt(6, vehicle.getRouteID());
            stmt.setDate(7, vehicle.getLastMaintenanceDate());

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all vehicles from the database.
     * @return a list of all vehicles
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        String sql = "SELECT v.*, vt.TypeName AS VehicleTypeName, ft.TypeName AS FuelTypeName FROM Vehicles v "
                + "LEFT JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID "
                + "LEFT JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = mapResultSetToVehicle(rs);
                vehicleList.add(vehicle);
                System.out.println("Vehicle Retrieved: " + vehicle.getVehicleNumber());
            }

            // Debug: Print the size of the list
            System.out.println("Vehicle List Size: " + vehicleList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    /**
     * Retrieves a single vehicle by its ID.
     * @param vehicleID the vehicle ID to search
     * @return a Vehicle object if found, null otherwise
     */
    public Vehicle getVehicleByID(int vehicleID) {
        String sql = "SELECT v.*, vt.TypeName AS VehicleTypeName, ft.TypeName AS FuelTypeName FROM Vehicles v "
                + "JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID "
                + "JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID "
                + "WHERE v.VehicleID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Get Vehicle by VehicleNumber
    public Vehicle getVehicleByVehicleNumber(String vehicleNumber) {
    String sql = "SELECT v.*, vt.TypeName AS VehicleTypeName, ft.TypeName AS FuelTypeName FROM Vehicles v " +
                 "JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID " +
                 "JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID " +
                 "WHERE v.VehicleNumber = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, vehicleNumber);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToVehicle(rs);  // 复用已有的映射函数
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }


    // Update Vehicle
    /**
     * Updates an existing vehicle in the database.
     * @param vehicle the updated vehicle object
     * @return true if the update was successful, false otherwise
     */
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE Vehicles SET VehicleNumber = ?, VehicleTypeID = ?, FuelTypeID = ?, ConsumptionRate = ?, MaxPassengers = ?, RouteID = ?, LastMaintenanceDate = ? WHERE VehicleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getVehicleNumber());
            stmt.setInt(2, vehicle.getVehicleType().getVehicleTypeID());
            stmt.setInt(3, vehicle.getFuelType().getFuelTypeID());
            stmt.setFloat(4, vehicle.getConsumptionRate());
            stmt.setInt(5, vehicle.getMaxPassengers());
            stmt.setObject(6, vehicle.getRouteID() != 0 ? vehicle.getRouteID() : null);
            stmt.setDate(7, vehicle.getLastMaintenanceDate());
            stmt.setInt(8, vehicle.getVehicleID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a vehicle from the database by ID.
     * @param vehicleID the ID of the vehicle to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteVehicle(int vehicleID) {
        String sql = "DELETE FROM Vehicles WHERE VehicleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Maps a ResultSet row to a Vehicle object.
     * @param rs the result set containing vehicle data
     * @return a Vehicle object
     * @throws SQLException if a database access error occurs
     */
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        int vehicleID = rs.getInt("VehicleID");
        String vehicleNumber = rs.getString("VehicleNumber");
        int vehicleTypeID = rs.getInt("VehicleTypeID");
        String vehicleTypeName = rs.getString("VehicleTypeName");
        int fuelTypeID = rs.getInt("FuelTypeID");
        String fuelTypeName = rs.getString("FuelTypeName");
        float consumptionRate = rs.getFloat("ConsumptionRate");
        int maxPassengers = rs.getInt("MaxPassengers");
        int routeID = rs.getObject("RouteID") != null ? rs.getInt("RouteID") : 0;
        Date lastMaintenanceDate = rs.getDate("LastMaintenanceDate");

        VehicleType vehicleType = new VehicleType(vehicleTypeID, vehicleTypeName);
        FuelType fuelType = new FuelType(fuelTypeID, fuelTypeName);

        // Create Vehicle object using Factory Design Pattern
        Vehicle vehicle = new Vehicle(vehicleID, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);
        System.out.println("Vehicle Retrieved: " + vehicleNumber);
        return vehicle;
    }
    
    /**
     * Checks whether a vehicle number already exists in the database.
     * @param vehicleNumber the vehicle number to check
     * @return true if it exists, false otherwise
     */
    public boolean isVehicleNumberExists(String vehicleNumber) {
        String sql = "SELECT COUNT(*) FROM Vehicles WHERE VehicleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicleNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves a vehicle by its vehicle number.
     * @param vehicleNumber the vehicle number to search
     * @return a Vehicle object if found, null otherwise
     */
    public Vehicle getVehicleByNumber(String vehicleNumber) {
    String sql = "SELECT * FROM ptfms.vehicles WHERE VehicleNumber = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, vehicleNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Vehicle v = new Vehicle();
            v.setVehicleID(rs.getInt("VehicleID"));
            v.setVehicleNumber(rs.getString("VehicleNumber"));
            v.setRouteID(rs.getInt("RouteID"));
            // 如果还有其他字段请一并补上
            return v;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
}
