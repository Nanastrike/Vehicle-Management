/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package module.GPS_Tracking;

import data.VehicleDAO;
import java.time.LocalDateTime;

/**
 * used to display the info in the front end
 *
 * @author silve
 */
public class TrackingDisplayDTO {

    private String vehicleNumber;
    private int routeID;
    private double position;
    private String destination;
    private String is_arrived;
    private LocalDateTime leavingTime;
    private LocalDateTime arriveTime;
    private int OperatorID;
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(int OperatorID) {
        this.OperatorID = OperatorID;
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
