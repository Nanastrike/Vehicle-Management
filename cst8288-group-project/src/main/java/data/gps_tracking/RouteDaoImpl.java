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

/**
 * Implementation of the RouteDao interface for accessing route-related data
 * from the database. This class retrieves route distance, name, start location,
 * and destination by using SQL queries against the Routes table.
 *
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05
 * @Description: Provides JDBC-based implementation of RouteDao interface
 * methods.
 */
public class RouteDaoImpl implements RouteDao {

    /**
     * JDBC connection to the database
     */
    private Connection conn;

    /**
     * Default constructor that initializes the database connection using a
     * singleton DatabaseConnection instance.
     */
    public RouteDaoImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Retrieves the total distance for a given route.
     *
     * @param routeID the route's unique identifier
     * @return the route distance as a double, or 0.0 if not found
     */
    @Override
    public double getRoadDistanceByRouteID(int routeID) {
        String sql = "SELECT Distance FROM Routes WHERE RouteID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, routeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Distance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Retrieves the destination (end location) for a given route.
     *
     * @param routeID the route's unique identifier
     * @return the end location string, or null if not found
     */
    @Override
    public String getRoadDestinationByID(int routeID) {
        String sql = "SELECT EndLocation FROM Routes WHERE RouteID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, routeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("EndLocation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the start location for a given route.
     *
     * @param routeID the route's unique identifier
     * @return the start location string, or null if not found
     */
    @Override
    public String getRoadStartByID(int routeID) {
        String sql = "SELECT StartLocation FROM Routes WHERE RouteID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, routeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("StartLocation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the display name of the route.
     *
     * @param routeID the route's unique identifier
     * @return the name of the route, or null if not found
     */
    @Override
    public String getRoadNameByID(int routeID) {
        String sql = "SELECT RouteName FROM Routes WHERE RouteID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, routeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RouteName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
