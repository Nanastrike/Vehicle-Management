/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 车辆行驶记录相关的参数
 *
 * @author silve
 */
public class VehicleActionDTO {

    private int vehicleID;
    private double carDistance;
    private LocalDateTime leavingTime;
    private LocalDateTime arriveTime;
    private LocalDateTime currentTime;
    private int OperatorID;
    private String operatorName;

    public VehicleActionDTO() {
    }

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

}
