/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.gps_tracking;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for representing vehicle action records. Used to
 * transfer data between the database and application logic, especially for
 * vehicle movement logs such as distance traveled, timestamps, and operator
 * information.
 *
 * @author  Qinyu Luo
 * @version 1.0
 * course CST8288
 * assignment Group Project
 * time 2025/04/05
 * Description Stores vehicle movement and operational data for logging or
 * display.
 */
public class VehicleActionDTO {

    /**
     * The ID of the vehicle involved in the action
     */
    private int vehicleID;

    /**
     * Total distance the vehicle has traveled (in kilometers or applicable
     * units)
     */
    private double carDistance;

    /**
     * The timestamp when the vehicle began moving
     */
    private LocalDateTime leavingTime;

    /**
     * The timestamp when the vehicle arrived at its destination
     */
    private LocalDateTime arriveTime;

    /**
     * The timestamp when this record was created or updated
     */
    private LocalDateTime currentTime;

    /**
     * The ID of the operator driving the vehicle
     */
    private int OperatorID;

    /**
     * The name of the operator (retrieved for display purposes)
     */
    private String operatorName;

    /**
     * Default no-arg constructor
     */
    public VehicleActionDTO() {
    }

    /**
     * Full constructor for creating a complete vehicle action record.
     *
     * @param vehicleID the vehicle's ID
     * @param carDistance the total distance traveled by the vehicle
     * @param leavingTime the time when the vehicle started its trip
     * @param arriveTime the time when the vehicle arrived (optional, can be
     * null)
     * @param currentTime the timestamp of this record (optional)
     * @param OperatorID the operator's ID
     * @param operatorName the operator's name
     */
    public VehicleActionDTO(int vehicleID, double carDistance, LocalDateTime leavingTime, LocalDateTime arriveTime, LocalDateTime currentTime, int OperatorID, String operatorName) {
        this.vehicleID = vehicleID;
        this.carDistance = carDistance;
        this.leavingTime = leavingTime;
        this.arriveTime = arriveTime;
        this.currentTime = currentTime;
        this.OperatorID = OperatorID;
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public double getCarDistance() {
        return carDistance;
    }

    public void setCarDistance(double carDistance) {
        this.carDistance = carDistance;
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

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public int getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(int OperatorID) {
        this.OperatorID = OperatorID;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

}
