/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.gps_tracking;

import data.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.GPS_Tracking.TrackingDisplayDTO;

/**
 * Implementation of the VehicleActionDao interface for managing GPS tracking
 * logs. This class interacts with the ptfms.gps_tracking table to insert,
 * retrieve, update, and delete vehicle movement records, and provides summary
 * statistics such as operator efficiency and currently running vehicle count.
 *
 * @author  Qinyu Luo
 * @version 1.0
 * course CST8288
 * assignment Group Project
 * time 2025/04/05
 * Description Full DAO implementation for vehicle tracking logic with JDBC.
 */
public class VehicleActionDaoImpl implements VehicleActionDao {

    /**
     * Database connection instance, injected or retrieved via singleton
     */
    private Connection conn;

    /**
     * Default constructor using singleton database connection.
     */
    public VehicleActionDaoImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Constructor accepting external connection (used for testing or reuse).
     *
     * @param conn the database connection
     */
    public VehicleActionDaoImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves the latest record for all vehicles from the gps_tracking table.
     * Only the most recent entry (by time) for each vehicle is selected.
     *
     * @return a list of VehicleActionDTO containing the latest logs for each
     * vehicle
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
     * Retrieves all GPS tracking logs for a specific vehicle, ordered by the
     * time the vehicle started driving (LeavingTime).
     *
     * This method is useful for showing the full trip history of a given
     * vehicle. It queries the gps_tracking table for all entries with the
     * specified VehicleID.
     *
     * @param vehicleID the unique ID of the vehicle whose logs are being
     * retrieved
     * @return a list of VehicleActionDTO objects representing the full trip
     * history
     */
    public List<VehicleActionDTO> getAllLogsByVehicleID(int vehicleID) {
        List<VehicleActionDTO> vehicleLogs = new ArrayList<>();
        String sql = "SELECT VehicleID, Position, LeavingTime, ArriveTime "
                + "FROM ptfms.gps_tracking "
                + "WHERE VehicleID = ? "
                + "ORDER BY LeavingTime ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                VehicleActionDTO vehicleAction = new VehicleActionDTO();
                vehicleAction.setVehicleID(rs.getInt("VehicleID"));
                vehicleAction.setCarDistance(rs.getDouble("Position"));

                //  Time fields — safe null handling for LocalDateTime
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
     * search a single vehicle history according to its vehicle id it helps the
     * program to decide whether inserts a new record or not if the present
     * vehicle doesn't arrive, the same vehicle will not add a new record
     *
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
     * Inserts a new GPS tracking record into the database. This method respects
     * null values for LeavingTime or ArriveTime.
     *
     * @param vehicle the DTO containing movement data to insert
     * @throws SQLException if a database error occurs
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

    /**
     * Deletes all GPS tracking logs associated with the specified vehicle.
     *
     * This operation removes all records from the gps_tracking table for the
     * given VehicleID. It is typically used when a vehicle is being removed
     * from the system or during data cleanup.
     *
     * @param vehicleID the ID of the vehicle whose logs should be deleted
     */
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

    /**
     * Updates the ongoing tracking log of a vehicle. This method modifies the
     * latest log entry that does not have an ArriveTime yet (i.e., the vehicle
     * has not reached its destination).
     *
     * It updates the position, leaving time, and optionally the arrive time.
     *
     * @param vehicle the VehicleActionDTO object containing the updated
     * tracking info
     */
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

    /**
     * Retrieves the leaving time of a given vehicle from the database, if it
     * exists.
     *
     * This method is used to ensure that LeavingTime is only set once per trip.
     * If the database already has a LeavingTime for the given vehicle, it will
     * return it; otherwise, the calling logic may proceed to generate and store
     * a new timestamp.
     *
     * @param vehicleID the ID of the vehicle to check for an existing
     * LeavingTime
     * @return the first found LeavingTime as LocalDateTime, or null if none
     * exists
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
     * Retrieves the latest GPS tracking log for each vehicle, and formats it as
     * a list of TrackingDisplayDTOs for front-end display.
     *
     * This method: - Joins data from GPS_Tracking, Vehicles, Users, and Routes
     * - Picks the most recent record per vehicle (via MAX(TrackingID)) -
     * Collects vehicle number, route ID, destination, timestamps, and operator
     * info
     *
     * @return a list of TrackingDisplayDTOs representing the current tracking
     * view
     */
    public List<TrackingDisplayDTO> getTrackingDisplayLogs() {
        List<TrackingDisplayDTO> logs = new ArrayList<>();

        //  SQL query explanation:
        // - For each vehicle, select its most recent tracking log
        // - Join user/operator info, vehicle details, and route destination
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
                //  Set vehicle info
                dto.setVehicleNumber(rs.getString("VehicleNumber"));
                dto.setRouteID(rs.getInt("RouteID"));
                dto.setPosition(rs.getDouble("Position"));

                //  Set timestamps (can be null)
                dto.setLeavingTime(rs.getObject("LeavingTime", LocalDateTime.class));
                dto.setArriveTime(rs.getObject("ArriveTime", LocalDateTime.class));

                //  Set route and operator info
                dto.setDestination(rs.getString("Destination"));
                dto.setOperatorName(rs.getString("OperatorName"));

                //  Mark arrival status based on presence of ArriveTime
                dto.setIs_arrived(dto.getArriveTime() != null ? "Y" : "N");
                logs.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    /**
     * Calculates the number of vehicles that are currently still running. A
     * vehicle is considered running if it has no ArriveTime or the ArriveTime
     * is in the future.
     *
     * @return the number of currently active vehicles
     * @throws SQLException if a query error occurs
     */
    @Override
    public int getRunningVehiclesCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM GPS_Tracking WHERE ArriveTime IS NULL OR ArriveTime > NOW()";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Retrieves a limited number of the most recent vehicle tracking records.
     *
     * This method fetches a list of recent actions from the GPS_Tracking table,
     * ordered by LeavingTime in descending order (latest first). It also joins
     * the Users table to retrieve the operator's name for display.
     *
     * @param limit the maximum number of records to return
     * @return a list of VehicleActionDTO objects representing recent vehicle
     * logs
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<VehicleActionDTO> getRecentVehicleActions(int limit) throws SQLException {
        String sql = """
        SELECT g.VehicleID, g.LeavingTime, g.ArriveTime, g.OperatorID, g.CurrentTime,
               u.Name AS OperatorName
        FROM GPS_Tracking g
        LEFT JOIN Users u ON g.OperatorID = u.UserID
        ORDER BY g.LeavingTime DESC
        LIMIT ?
    """;

        List<VehicleActionDTO> actions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VehicleActionDTO action = new VehicleActionDTO();
                action.setVehicleID(rs.getInt("VehicleID"));

                Timestamp leaving = rs.getTimestamp("LeavingTime");
                if (leaving != null) {
                    action.setLeavingTime(leaving.toLocalDateTime());
                }

                Timestamp arrive = rs.getTimestamp("ArriveTime");
                if (arrive != null) {
                    action.setArriveTime(arrive.toLocalDateTime());
                }

                Timestamp current = rs.getTimestamp("CurrentTime");
                if (current != null) {
                    action.setCurrentTime(current.toLocalDateTime());
                }

                action.setOperatorID(rs.getInt("OperatorID"));
                action.setOperatorName(rs.getString("OperatorName")); // from JOINed Users.Name

                actions.add(action);
            }
        }
        return actions;
    }

    /**
     * Calculates operator efficiency by summing the distance driven by each
     * operator.
     *
     * @return a map of operator names to their total driven distances
     * @throws SQLException if the query fails
     */
    @Override
    public Map<String, Double> calculateOperatorEfficiency() throws SQLException {
        String sql = "SELECT u.Name AS OperatorName, SUM(gt.Position) AS TotalDistance "
                + "FROM GPS_Tracking gt "
                + "JOIN Users u ON gt.OperatorID = u.UserID "
                + "GROUP BY gt.OperatorID, u.Name";
        Map<String, Double> efficiencyMap = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("OperatorName");
                double totalDistance = rs.getDouble("TotalDistance");
                efficiencyMap.put(name, totalDistance);
            }
        }
        return efficiencyMap;
    }
}
