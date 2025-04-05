/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data.gps_tracking;

/**
 * Data Access Object (DAO) interface for retrieving route-related information.
 * Provides methods to query a route's distance, name, start point, and destination
 * by its unique ID.
 * 
 * This interface is used by vehicle tracking logic to determine progress,
 * evaluate arrival, and display route metadata.
 *
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05
 * @Description: Defines data access methods for route information used in vehicle tracking.
 */
public interface RouteDao {
    /**
     * Returns the total distance of the route in kilometers (or applicable units).
     *
     * @param routeID the ID of the route
     * @return the total length of the route
     */
    double getRoadDistanceByRouteID(int routeID);
    
    /**
     * Returns the destination (end location) of the route.
     *
     * @param routeID the ID of the route
     * @return the destination name
     */
    String getRoadDestinationByID(int routeID);
    
    /**
     * Returns the start location of the route.
     *
     * @param routeID the ID of the route
     * @return the starting point name
     */
    String getRoadStartByID(int routeID);
    
    /**
     * Returns the display name of the route (e.g., "Downtown Loop").
     *
     * @param routeID the ID of the route
     * @return the name of the route
     */
    String getRoadNameByID(int routeID);
}
