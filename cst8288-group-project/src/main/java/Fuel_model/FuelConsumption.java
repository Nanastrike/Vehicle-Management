/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_model;

import java.sql.Timestamp;

public class FuelConsumption {
    private int consumptionId;
    private int vehicleId;
    private int fuelTypeId;
    private float fuelUsed;
    private float distanceTraveled;
    private Timestamp timestamp;
    private String status;

    // Constructors
    public FuelConsumption() {}

    public FuelConsumption(int vehicleId, int fuelTypeId, float fuelUsed, float distanceTraveled, Timestamp timestamp) {
        this.vehicleId = vehicleId;
        this.fuelTypeId = fuelTypeId;
        this.fuelUsed = fuelUsed;
        this.distanceTraveled = distanceTraveled;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(int consumptionId) {
        this.consumptionId = consumptionId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(int fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public float getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(float fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(float distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
