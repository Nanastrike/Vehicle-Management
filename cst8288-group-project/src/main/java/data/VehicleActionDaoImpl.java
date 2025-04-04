/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.GPS_Tracking.TrackingDisplayDTO;

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

        String sql = """
                SELECT g.VehicleID, g.Position, g.LeavingTime, g.ArriveTime, u.Name AS OperatorName
                FROM ptfms.gps_tracking g
                LEFT JOIN ptfms.Users u ON g.OperatorID = u.UserID
                ORDER BY COALESCE(g.LeavingTime, g.CurrentTime) ASC
                """;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleActionDTO vehicleAction = new VehicleActionDTO();

                vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                vehicleAction.setCarDistance(rs.getDouble("Position"));

                // 数据库里 "LeavingTime" 列是 TIME 或 DATETIME 类型
                LocalDateTime leavingTime = rs.getObject("LeavingTime", LocalDateTime.class);
                vehicleAction.setLeavingTime(leavingTime);

                // 数据库里 "ArriveTime" 列也存的是 TIME 或 DATETIME
                LocalDateTime arriveTime = rs.getObject("ArriveTime", LocalDateTime.class);
                vehicleAction.setArriveTime(arriveTime);
                vehicleAction.setOperatorName(rs.getString("OperatorName"));

                vehicleLogs.add(vehicleAction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleLogs;
    }
    
    
    /**
     * return all the vehicle logs by vehicle id
     * @param vehicleID
     * @return 
     */
public List<VehicleActionDTO> getAllLogsByVehicleID(int vehicleID) {
    List<VehicleActionDTO> vehicleLogs = new ArrayList<>();
    String sql = "SELECT VehicleID, Position, LeavingTime, ArriveTime " +
                 "FROM ptfms.gps_tracking " +
                 "WHERE VehicleID = ? " +
                 "ORDER BY LeavingTime ASC";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, vehicleID);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            VehicleActionDTO vehicleAction = new VehicleActionDTO();
            vehicleAction.setVehicleID(rs.getInt("VehicleID"));
            vehicleAction.setCarDistance(rs.getDouble("Position"));

            LocalDateTime leavingTime = rs.getObject("LeavingTime", LocalDateTime.class);
            vehicleAction.setLeavingTime(leavingTime);

            LocalDateTime arriveTime = rs.getObject("ArriveTime", LocalDateTime.class);
            vehicleAction.setArriveTime(arriveTime);

            vehicleLogs.add(vehicleAction);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return vehicleLogs;
}

    /**
     * search a single vehicle history according to its vehicle id
     * it helps the program to decide whether inserts a new record or not
     * if the present vehicle doesn't arrive, the same vehicle will not add a new record
     * @param vehicleID
     * @return
     */
    @Override
    public VehicleActionDTO getVehicleLogs(int vehicleID) {
        VehicleActionDTO vehicleAction = null;
        String sql = "SELECT VehicleID, Position, LeavingTime, ArriveTime "
                + "FROM ptfms.gps_tracking WHERE VehicleID = ? AND ArriveTime IS NULL "
                + "ORDER BY TrackingID DESC LIMIT 1";
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
                    LocalDateTime leavingTime = rs.getObject("LeavingTime", LocalDateTime.class);
                    vehicleAction.setLeavingTime(leavingTime);

                    LocalDateTime arriveTime = rs.getObject("ArriveTime", LocalDateTime.class);
                    vehicleAction.setArriveTime(arriveTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleAction;
    }

    /**
     * insert new record in database
     * @param vehicle
     * @throws SQLException 
     */
    @Override
    public void insertDistanceLog(VehicleActionDTO vehicle) throws SQLException {
        String sql = "INSERT INTO ptfms.gps_tracking (VehicleID, Position, LeavingTime, ArriveTime, OperatorID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicle.getVehicleID());
            stmt.setDouble(2, vehicle.getCarDistance());
            if (vehicle.getLeavingTime() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(vehicle.getLeavingTime()));
            } else {
                stmt.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if (vehicle.getArriveTime() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(vehicle.getArriveTime()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            stmt.setInt(5, vehicle.getOperatorID());
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
        String sql = "UPDATE ptfms.gps_tracking "
                + "SET Position = ?, LeavingTime = ?, ArriveTime = ? "
                + "WHERE VehicleID = ? AND ArriveTime IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, vehicle.getCarDistance());

            if (vehicle.getLeavingTime() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(vehicle.getLeavingTime()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            if (vehicle.getArriveTime() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(vehicle.getArriveTime()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setInt(4, vehicle.getVehicleID());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VehicleActionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     * 查询数据库中是否已经有该车辆的 LeavingTime，如果有就用数据库里的，
     * 不再生成新的；如果没有，就生成一次并存进去。
     * @param vehicleID
     * @return 
     */
    @Override
    public LocalDateTime getLeavingTimeFromDB(int vehicleID) {
        String sql = "SELECT LeavingTime FROM ptfms.gps_tracking WHERE VehicleID = ? AND LeavingTime IS NOT NULL LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject("LeavingTime", LocalDateTime.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 数据库里没有记录
    }

    /**
     * return all the necessary info for front end page
     *
     * @return all the necessary info for front end page
     */
    public List<TrackingDisplayDTO> getTrackingDisplayLogs() {
        List<TrackingDisplayDTO> logs = new ArrayList<>();

        String sql = """
        SELECT g.VehicleID, g.Position, g.LeavingTime, g.ArriveTime, g.OperatorID, u.Name AS OperatorName, v.RouteID, v.VehicleNumber, r.EndLocation AS Destination
        FROM ptfms.gps_tracking g
        LEFT JOIN ptfms.Users u ON g.OperatorID = u.UserID
        LEFT JOIN ptfms.Vehicles v ON g.VehicleID = v.VehicleID
        LEFT JOIN ptfms.Routes r ON v.RouteID = r.RouteID
        WHERE g.TrackingID IN (
            SELECT MAX(TrackingID) FROM ptfms.gps_tracking GROUP BY VehicleID
        )
        ORDER BY g.VehicleID;
    """;

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TrackingDisplayDTO dto = new TrackingDisplayDTO();
                dto.setVehicleNumber(rs.getString("VehicleNumber"));
                dto.setRouteID(rs.getInt("RouteID"));
                dto.setPosition(rs.getDouble("Position"));

                // 时间字段处理
                dto.setLeavingTime(rs.getObject("LeavingTime", LocalDateTime.class));
                dto.setArriveTime(rs.getObject("ArriveTime", LocalDateTime.class));

                dto.setDestination(rs.getString("Destination"));
                dto.setOperatorName(rs.getString("OperatorName"));

                dto.setIs_arrived(dto.getArriveTime() != null ? "Y" : "N");

                logs.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

}
