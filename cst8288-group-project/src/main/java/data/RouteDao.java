/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data;

/**
 *
 * @author silve
 */
public interface RouteDao {
    /**
     * return the total length of the rode
     * @param routeID
     * @return the total length of the rode
     */
    double getRoadDistanceByRouteID(int routeID);
    
    /**
     * return the destination of the rode
     * @param routeID
     * @return 
     */
    String getRoadDestinationByID(int routeID);
    
    /**
     * return the start location of the rode
     * @param routeID
     * @return 
     */
    String getRoadStartByID(int routeID);
    
    /**
     * return the name of the rode
     * @param routeID
     * @return the name of the rode
     */
    String getRoadNameByID(int routeID);
}
