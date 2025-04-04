package Fuel_dao;

import Fuel_model.FuelConsumption;
import data.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for performing CRUD operations on the Fuel_Consumption table.
 */
public class FuelConsumptionDAO {

    private Connection conn;

    /**
     * Constructs a FuelConsumptionDAO and establishes a database connection.
     */
    public FuelConsumptionDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Inserts a new fuel consumption record into the database.
     *
     * @param fuel The FuelConsumption object to insert.
     */
    public void insertFuelConsumption(FuelConsumption fuel) {
        String sql = "INSERT INTO Fuel_Consumption (VehicleID, FuelTypeID, FuelUsed, DistanceTraveled, Timestamp, Status) VALUES (?, ?, ?, ?, ?, ?)";
        


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fuel.getVehicleId());
            stmt.setInt(2, fuel.getFuelTypeId());
            stmt.setDouble(3, fuel.getFuelUsed());
            stmt.setDouble(4, fuel.getDistanceTraveled());
            stmt.setTimestamp(5, new Timestamp(fuel.getTimestamp().getTime()));
            stmt.setString(6, fuel.getStatus());
            stmt.executeUpdate();
            System.out.println("Fuel consumption inserted for VehicleID: " + fuel.getVehicleId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all fuel consumption records from the database.
     *
     * @return A list of FuelConsumption objects.
     */
    public List<FuelConsumption> getAllFuelConsumption() {
        List<FuelConsumption> list = new ArrayList<>();
        String sql = "SELECT * FROM Fuel_Consumption";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FuelConsumption fc = new FuelConsumption();
                fc.setConsumptionId(rs.getInt("ConsumptionID"));
                fc.setVehicleId(rs.getInt("VehicleID"));
                fc.setFuelTypeId(rs.getInt("FuelTypeID"));
                fc.setFuelUsed(rs.getFloat("FuelUsed"));
                fc.setDistanceTraveled(rs.getFloat("DistanceTraveled"));
                fc.setTimestamp(rs.getTimestamp("Timestamp"));
                fc.setStatus(rs.getString("Status"));

                list.add(fc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Updates an existing fuel consumption record in the database.
     *
     * @param fc The FuelConsumption object containing updated values.
     * @return True if the update was successful; otherwise, false.
     */
    public boolean updateFuelConsumption(FuelConsumption fc) {
         String sql = "UPDATE Fuel_Consumption SET VehicleID=?, FuelTypeID=?, FuelUsed=?, DistanceTraveled=?, Timestamp=?, Status=? WHERE ConsumptionID=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fc.getVehicleId());
            stmt.setInt(2, fc.getFuelTypeId());
            stmt.setFloat(3, fc.getFuelUsed());
            stmt.setFloat(4, fc.getDistanceTraveled());
            stmt.setTimestamp(5, fc.getTimestamp());
            stmt.setString(6, fc.getStatus());
            stmt.setInt(7, fc.getConsumptionId());


            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a fuel consumption record by its ID.
     *
     * @param consumptionId The ID of the consumption record to delete.
     * @return True if deletion was successful; otherwise, false.
     */
    public boolean deleteFuelConsumption(int consumptionId) {
        String sql = "DELETE FROM Fuel_Consumption WHERE ConsumptionID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consumptionId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a single fuel consumption record by its ID.
     *
     * @param id The ID of the consumption record.
     * @return A FuelConsumption object if found; otherwise, null.
     */
    public FuelConsumption getFuelConsumptionById(int id) {
        FuelConsumption fc = null;
        String sql = "SELECT * FROM Fuel_Consumption WHERE ConsumptionID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fc = new FuelConsumption();
                fc.setConsumptionId(rs.getInt("ConsumptionID"));
                fc.setVehicleId(rs.getInt("VehicleID"));
                fc.setFuelTypeId(rs.getInt("FuelTypeID"));
                fc.setFuelUsed(rs.getFloat("FuelUsed"));
                fc.setDistanceTraveled(rs.getFloat("DistanceTraveled"));
                fc.setTimestamp(rs.getTimestamp("Timestamp"));
                fc.setStatus(rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fc;
    }
}
