/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import jakarta.activation.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silve
 */
public class VehicleActionDaoImpl implements VehicleActionDao {

    private Connection conn;

    /**
     * Retrieves all vehicle logs
     *
     * @return
     */
    @Override
    public List<VehicleActionDTO> getAllVehicleLogs() {
        List<VehicleActionDTO> vehicleLogs = new ArrayList<>();

        String sql = "SELECT vehicleID,carDistance,leavingTime,arriveTime FROM gps_tracking";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleActionDTO vehicleAction = new VehicleActionDTO();

                vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                vehicleAction.setCarDistance(rs.getDouble("CarDistance"));

                // 数据库里 "LeavingTime" 列是 TIME 或 DATETIME 类型
                LocalTime leavingTime = rs.getObject("LeavingTime", LocalTime.class);
                vehicleAction.setLeavingTime(leavingTime);

                // 数据库里 "ArriveTime" 列也存的是 TIME 或 DATETIME
                LocalTime arriveTime = rs.getObject("ArriveTime", LocalTime.class);
                vehicleAction.setArriveTime(arriveTime);

                vehicleLogs.add(vehicleAction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleLogs;
    }

    /**
     * search the vehicle history according to its vehicle id
     * @param vehicleID
     * @return 
     */
    @Override
    public VehicleActionDTO getVehicleLogs(int vehicleID) {
        VehicleActionDTO vehicleAction = null;
        String sql = "SELECT vehicleID,carDistance,leavingTime,arriveTime"
                +"FROM gps_tracking WHERE vehicleID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        // 给 WHERE vehicleID=? 的占位符赋值
        pstmt.setInt(1, vehicleID);

        try (ResultSet rs = pstmt.executeQuery()) {
            // 如果只取第一条，就用 if (rs.next())；如果可能有多条，就 while(rs.next())
            if (rs.next()) {
                vehicleAction = new VehicleActionDTO();
                vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                vehicleAction.setCarDistance(rs.getDouble("CarDistance"));

                // 取 TIME/DATETIME 列为 LocalTime，如果数据库的列类型匹配
                LocalTime leavingTime = rs.getObject("LeavingTime", LocalTime.class);
                vehicleAction.setLeavingTime(leavingTime);

                LocalTime arriveTime = rs.getObject("ArriveTime", LocalTime.class);
                vehicleAction.setArriveTime(arriveTime);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return vehicleAction;
}

    @Override
    public void insertDistanceLog(VehicleActionDTO vehicle) throws SQLException {
        String sql = "INSERT INTO gps_tracking (VehicleID,Position, LeavingTime"
                +"ArriveTime) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getVehicleID());
            stmt.setDouble(2, vehicle.getCarDistance());
            stmt.setTime(3, Time.valueOf(vehicle.getLeavingTime()));
            stmt.setTime(4, Time.valueOf(vehicle.getArriveTime()));
            stmt.executeUpdate();
        }

    }

    @Override
    public void deleteVehicleLogs(int vehicleID) {
        String sql = "DELETE FROM gps_tracking WHERE vehicleID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleID);
            pstmt.executeUpdate();
            System.out.println("log deleted successfully");
        } catch (SQLException ex) {
            Logger.getLogger(VehicleActionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @Override
    public void updateVehicleLogs(VehicleActionDTO vehicle) {
        String sql = "UPDATE gps_tracking SET vehicleID = ?,leavingTime=?,ArriveTime=?,"
                +"WHERE vehicleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getVehicleID());
            stmt.setDouble(2, vehicle.getCarDistance());
            stmt.setTime(3, Time.valueOf(vehicle.getLeavingTime()));
            stmt.setTime(4, Time.valueOf(vehicle.getArriveTime()));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleActionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

}
