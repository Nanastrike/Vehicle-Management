package data;

import model.VehicleManagement.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private Connection conn;

    // Constructor to accept a connection
    public VehicleDAO(Connection conn) {
        this.conn = conn;
    }

    // Add Vehicle
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


    // Get All Vehicles
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        String sql = "SELECT v.*, vt.TypeName AS VehicleTypeName, ft.TypeName AS FuelTypeName FROM Vehicles v " +
                     "LEFT JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID " +
                     "LEFT JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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

    // Get Vehicle by ID
    public Vehicle getVehicleByID(int vehicleID) {
        String sql = "SELECT v.*, vt.TypeName AS VehicleTypeName, ft.TypeName AS FuelTypeName FROM Vehicles v " +
                     "JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID " +
                     "JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID " +
                     "WHERE v.VehicleID = ?";

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

    // Update Vehicle
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

    // Delete Vehicle
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

    // Map ResultSet to Vehicle Object
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
    // Check if Vehicle Number Already Exists
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
}