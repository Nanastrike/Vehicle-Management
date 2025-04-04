/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_model;

public class Vehicle {

    private int vehicleId;
    private String vehicleType;
    private String fuelType;
    private double consumptionRate;  // Bus / Light Rail 用
    private double dieselRate;       // Diesel-Electric Train 用
    private double electricRate;

    // --- 构造函数 ---
    public Vehicle() {} // 可用于 DAO 查询后逐个 set

    public Vehicle(int vehicleId, String vehicleType, String fuelType, double consumptionRate) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.consumptionRate = consumptionRate;
    }

    public Vehicle(int vehicleId, String vehicleType, double dieselRate, double electricRate) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.dieselRate = dieselRate;
        this.electricRate = electricRate;
    }

    public Vehicle(int vehicleId, String vehicleType, String fuelType, double consumptionRate, double dieselRate, double electricRate) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.consumptionRate = consumptionRate;
        this.dieselRate = dieselRate;
        this.electricRate = electricRate;
    }
    


    // --- Getters 和 Setters ---
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(double consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public double getDieselRate() {
        return dieselRate;
    }

    public void setDieselRate(double dieselRate) {
        this.dieselRate = dieselRate;
    }

    public double getElectricRate() {
        return electricRate;
    }

    public void setElectricRate(double electricRate) {
        this.electricRate = electricRate;
    }
    // ... 略，可自动生成 ...
}

