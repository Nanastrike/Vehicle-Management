package Fuel_dao;

import Fuel_model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private Connection conn;

    public VehicleDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null) {
            System.err.println("❌ VehicleDAO - Database connection is NULL");
        } else {
            System.out.println("✅ VehicleDAO - Database connection is established");
        }
    } // 👈 这一行之前你漏掉了！

    // 根据 VehicleID 获取完整 Vehicle 对象
    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT v.VehicleID, vt.TypeName AS VehicleType, " +
                     "ft.TypeName AS FuelType, v.ConsumptionRate, " +
                     "v.DieselRate, v.ElectricRate " +
                     "FROM Vehicles v " +
                     "JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID " +
                     "JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID " +
                     "WHERE v.VehicleID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                    rs.getInt("VehicleID"),
                    rs.getString("VehicleType"),
                    rs.getString("FuelType"),
                    rs.getDouble("ConsumptionRate"),
                    rs.getDouble("DieselRate"),
                    rs.getDouble("ElectricRate")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取所有车辆用于下拉菜单
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT v.VehicleID, vt.TypeName AS VehicleType, " +
                     "ft.TypeName AS FuelType, v.ConsumptionRate, " +
                     "v.DieselRate, v.ElectricRate " +
                     "FROM Vehicles v " +
                     "JOIN VehicleTypes vt ON v.VehicleTypeID = vt.VehicleTypeID " +
                     "JOIN FuelTypes ft ON v.FuelTypeID = ft.FuelTypeID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle(
                    rs.getInt("VehicleID"),
                    rs.getString("VehicleType"),
                    rs.getString("FuelType"),
                    rs.getDouble("ConsumptionRate"),
                    rs.getDouble("DieselRate"),
                    rs.getDouble("ElectricRate")
                );
                vehicles.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }
}
