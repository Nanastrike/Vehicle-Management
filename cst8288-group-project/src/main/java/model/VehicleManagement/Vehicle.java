package model.VehicleManagement;

import factory.VehicleFactory;

import java.sql.Date;

public class Vehicle {
    private int vehicleID;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private FuelType fuelType;
    private float consumptionRate;
    private int maxPassengers;
    private int routeID;
    private Date lastMaintenanceDate;
    private VehicleInterface vehicleInterface; // Factory-based vehicle type

    // Constructor
    public Vehicle() {}

    public Vehicle(int vehicleID, String vehicleNumber, VehicleType vehicleType, FuelType fuelType,
                   float consumptionRate, int maxPassengers, int routeID, Date lastMaintenanceDate) {
        this.vehicleID = vehicleID;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.consumptionRate = consumptionRate;
        this.maxPassengers = maxPassengers;
        this.routeID = routeID;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.vehicleInterface = VehicleFactory.getVehicle(VehicleTypeEnum.valueOf(vehicleType.getTypeName().toUpperCase().replace(" ", "_")));
    }

    // Getter to get vehicle information from Factory
    public void displayVehicleInfo() {
        vehicleInterface.displayVehicleInfo();
    }

    // Getters and Setters (same as before)
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        this.vehicleInterface = VehicleFactory.getVehicle(VehicleTypeEnum.valueOf(vehicleType.getTypeName().toUpperCase().replace(" ", "_")));
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public float getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(float consumptionRate) {
        this.consumptionRate = consumptionRate;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
}
