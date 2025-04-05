/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

import java.time.LocalDateTime;

/**
 * @author Qinyu Luo
 * @version 1.0
 * @since 2025/04/05
 * 
 * course CST8288
 * assignment group project
 * 
 * description Holds vehicle tracking data used for presentation in the
 * tracking interface. Data Transfer Object (DTO) for displaying real-time
 * vehicle tracking information. This class encapsulates various details such as
 * vehicle number, route, position, destination, arrival status, operator info,
 * and timing, primarily for front-end display.
 */
public class TrackingDisplayDTO {

    /**
     * The unique identifier for the vehicle (e.g., license plate or unit
     * number)
     */
    private String vehicleNumber;

    /**
     * ID of the assigned route for the vehicle
     */
    private int routeID;

    /**
     * Current position of the vehicle, represented as distance or coordinate
     */
    private double position;

    /**
     * Destination name or endpoint of the route
     */
    private String destination;

    /**
     * Indicates whether the vehicle has arrived ("yes"/"no" or similar)
     */
    private String is_arrived;

    /**
     * The timestamp when the vehicle started the trip
     */
    private LocalDateTime leavingTime;

    /**
     * The timestamp when the vehicle arrived at its destination
     */
    private LocalDateTime arriveTime;

    /**
     * ID of the operator (driver) currently assigned to this vehicle
     */
    private int operatorID; // Note: naming could be changed to 'operatorID' for consistency

    /**
     * Name of the operator (driver)
     */
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(int operatorID) {
        this.operatorID = operatorID;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getIs_arrived() {
        return is_arrived;
    }

    public void setIs_arrived(String is_arrived) {
        this.is_arrived = is_arrived;
    }

    public LocalDateTime getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(LocalDateTime leavingTime) {
        this.leavingTime = leavingTime;
    }

    public LocalDateTime getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(LocalDateTime arriveTime) {
        this.arriveTime = arriveTime;
    }

}
