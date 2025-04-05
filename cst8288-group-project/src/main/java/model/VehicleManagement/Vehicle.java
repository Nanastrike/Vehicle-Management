package model.VehicleManagement;

import factory.VehicleFactory;

import java.sql.Date;

/**
 * Represents a vehicle in the transit fleet system.
 * Each vehicle has associated details such as type, fuel, consumption rate,
 * passenger capacity, route, and last maintenance date.
 *
 * <p>Uses the Factory Design Pattern through {@link factory.VehicleFactory}
 * to dynamically assign behavior based on {@link VehicleTypeEnum}.</p>
 *
 * <p>This class is often used in conjunction with {@link VehicleType} and {@link FuelType}
 * and corresponds to records in the {@code Vehicles} database table.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see factory.VehicleFactory
 * @see model.VehicleManagement.VehicleType
 * @see model.VehicleManagement.FuelType
 */
public class Vehicle {
    private int vehicleID;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private FuelType fuelType;
    private float consumptionRate;
    private int maxPassengers;
    private int routeID;
    private Date lastMaintenanceDate;
    private double dieselRate;   // new
    private double electricRate; //new
    private VehicleInterface vehicleInterface; // Factory-based vehicle type

    /**
     * Default constructor.
     */
    public Vehicle() {}

    /**
     * Full constructor to initialize a Vehicle object.
     *
     * @param vehicleID the vehicle ID
     * @param vehicleNumber the vehicle number
     * @param vehicleType the type of vehicle
     * @param fuelType the fuel type used
     * @param consumptionRate the rate of fuel/energy consumption
     * @param maxPassengers the max number of passengers
     * @param routeID the assigned route ID
     * @param lastMaintenanceDate the date of last maintenance
     */
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
        this.vehicleInterface = VehicleFactory.getVehicle(
        VehicleTypeEnum.valueOf(vehicleType.getTypeName().toUpperCase().replace("-", "_").replace(" ", "_"))
    );

    }

    /**
     * Displays vehicle information using the Factory-generated vehicle behavior.
     */
    public void displayVehicleInfo() {
        vehicleInterface.displayVehicleInfo();
    }

    // Getters and Setters

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
        this.vehicleInterface = VehicleFactory.getVehicle(
                VehicleTypeEnum.valueOf(vehicleType.getTypeName().toUpperCase().replace(" ", "_")));
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
}
