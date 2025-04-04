/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_dao;

import Fuel_model.FuelConsumption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuelConsumptionDAO {

    public void insertFuelConsumption(FuelConsumption fuel) {
        String sql = "INSERT INTO Fuel_Consumption (VehicleID, FuelTypeID, FuelUsed, DistanceTraveled, Timestamp) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fuel.getVehicleId());
            stmt.setInt(2, fuel.getFuelTypeId());
            stmt.setDouble(3, fuel.getFuelUsed());
            stmt.setDouble(4, fuel.getDistanceTraveled());
            stmt.setTimestamp(5, new Timestamp(fuel.getTimestamp().getTime()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

            list.add(fc);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


// Update an existing fuel consumption record

    private Connection conn;

    public FuelConsumptionDAO() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean updateFuelConsumption(FuelConsumption fc) {
        String sql = "UPDATE Fuel_Consumption SET VehicleID=?, FuelTypeID=?, FuelUsed=?, DistanceTraveled=?, Timestamp=? WHERE ConsumptionID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fc.getVehicleId());
            stmt.setInt(2, fc.getFuelTypeId());
            stmt.setFloat(3, fc.getFuelUsed());
            stmt.setFloat(4, fc.getDistanceTraveled());
            stmt.setTimestamp(5, fc.getTimestamp());
            stmt.setInt(6, fc.getConsumptionId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete a fuel consumption record by ID
    public boolean deleteFuelConsumption(int consumptionId) {
        String sql = "DELETE FROM fuel_consumption WHERE ConsumptionID = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consumptionId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fc;
    }
   

}
