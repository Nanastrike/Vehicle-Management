/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.time.LocalTime;

/**
 * 车辆行驶记录相关的参数
 * @author silve
 */
public class VehicleActionDTO {
    
    private int vehicleID;
    private double carDistance;
    private LocalTime leavingTime;
    private LocalTime arriveTime;
    private LocalTime currentTime;

    public VehicleActionDTO() {
    }

    public VehicleActionDTO(int vehicleID, double carDistance, LocalTime leavingTime, LocalTime arriveTime, LocalTime currentTime) {
        this.vehicleID = vehicleID;
        this.carDistance = carDistance;
        this.leavingTime = leavingTime;
        this.arriveTime = arriveTime;
        this.currentTime = currentTime;
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

    public LocalTime getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(LocalTime leavingTime) {
        this.leavingTime = leavingTime;
    }

    public LocalTime getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(LocalTime arriveTime) {
        this.arriveTime = arriveTime;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }
    
    
    
}
