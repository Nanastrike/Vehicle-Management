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
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public VehicleActionDaoImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public VehicleActionDaoImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves all vehicle logs
     *
     * @return
     */
    @Override
    public List<VehicleActionDTO> getAllVehicleLogs() {
        List<VehicleActionDTO> vehicleLogs = new ArrayList<>();

        String sql = "SELECT VehicleID,Position,LeavingTime,ArriveTime FROM ptfms.gps_tracking";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleActionDTO vehicleAction = new VehicleActionDTO();

                vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                vehicleAction.setCarDistance(rs.getDouble("Position"));

                // 数据库里 "LeavingTime" 列是 TIME 或 DATETIME 类型
                LocalDateTime  leavingTime = rs.getObject("LeavingTime", LocalDateTime .class);
                vehicleAction.setLeavingTime(leavingTime);

                // 数据库里 "ArriveTime" 列也存的是 TIME 或 DATETIME
                LocalDateTime  arriveTime = rs.getObject("ArriveTime", LocalDateTime .class);
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
     *
     * @param vehicleID
     * @return
     */
    @Override
    public VehicleActionDTO getVehicleLogs(int vehicleID) {
        VehicleActionDTO vehicleAction = null;
        String sql = "SELECT VehicleID, Position,LeavingTime,ArriveTime "
                + "FROM ptfms.gps_tracking WHERE VehicleID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 给 WHERE vehicleID=? 的占位符赋值
            pstmt.setInt(1, vehicleID);

            try (ResultSet rs = pstmt.executeQuery()) {
                // 如果只取第一条，就用 if (rs.next())；如果可能有多条，就 while(rs.next())
                if (rs.next()) {
                    vehicleAction = new VehicleActionDTO();
                    vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                    vehicleAction.setCarDistance(rs.getDouble("Position"));

                    // 取 TIME/DATETIME 列为 LocalTime，如果数据库的列类型匹配
                    LocalDateTime  leavingTime = rs.getObject("LeavingTime", LocalDateTime .class);
                    vehicleAction.setLeavingTime(leavingTime);

                    LocalDateTime  arriveTime = rs.getObject("ArriveTime", LocalDateTime .class);
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
        String sql = "INSERT INTO ptfms.gps_tracking (VehicleID, Position, LeavingTime, ArriveTime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getVehicleID());
            stmt.setDouble(2, vehicle.getCarDistance());
            if (vehicle.getLeavingTime() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(vehicle.getLeavingTime()));
            } else {
                stmt.setNull(3, java.sql.Types.TIME);
            }

            if (vehicle.getArriveTime() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(vehicle.getArriveTime()));
            } else {
                stmt.setNull(4, java.sql.Types.TIME);
            }
            stmt.executeUpdate();
        }

    }

    @Override
    public void deleteVehicleLogs(int vehicleID) {
        String sql = "DELETE FROM ptfms.gps_tracking WHERE VehicleID=?";
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
        String sql = "UPDATE ptfms.gps_tracking SET VehicleID = ?, LeavingTime = ?, ArriveTime = ? WHERE VehicleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getVehicleID());
            stmt.setDouble(2, vehicle.getCarDistance());
            stmt.setTimestamp(3, Timestamp.valueOf(vehicle.getLeavingTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(vehicle.getArriveTime()));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleActionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
