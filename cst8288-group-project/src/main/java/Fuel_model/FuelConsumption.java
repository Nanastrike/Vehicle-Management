/**
 * File: FuelConsumption.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class represents a fuel or energy consumption record for a vehicle in the public transit fleet.
 * It includes details such as vehicle ID, fuel type, amount of fuel used, distance traveled,
 * timestamp of the record, and a status classification (e.g., Normal, Warning, Critical).
 * This model is used for tracking and evaluating fuel efficiency over time.
 */

package Fuel_model;

import java.sql.Timestamp;

/**
 * Represents a fuel or energy consumption entry in the transit fleet monitoring system.
 */
public class FuelConsumption {
    private int consumptionId;
    private int vehicleId;
    private int fuelTypeId;
    private float fuelUsed;
    private float distanceTraveled;
    private Timestamp timestamp;
    private String status;


    /**
    * Default constructor.
    */
    public FuelConsumption() {}
    
    /**
     * Constructs a FuelConsumption object with full initialization (excluding ID and status).
     *
     * @param vehicleId         ID of the vehicle
     * @param fuelTypeId        ID of the fuel type
     * @param fuelUsed          Amount of fuel or energy used
     * @param distanceTraveled  Distance traveled during the measurement
     * @param timestamp         Timestamp of the record
     */
    public FuelConsumption(int vehicleId, int fuelTypeId, float fuelUsed, float distanceTraveled, Timestamp timestamp) {
        this.vehicleId = vehicleId;
        this.fuelTypeId = fuelTypeId;
        this.fuelUsed = fuelUsed;
        this.distanceTraveled = distanceTraveled;
        this.timestamp = timestamp;
    }

   /**
     * Gets the unique consumption record ID.
     *
     * @return consumption ID
     */
    public int getConsumptionId() {
        return consumptionId;
    }

    /**
     * Sets the unique consumption record ID.
     *
     * @param consumptionId the ID to set
     */
    public void setConsumptionId(int consumptionId) {
        this.consumptionId = consumptionId;
    }

    /**
     * Gets the vehicle ID.
     *
     * @return vehicle ID
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets the vehicle ID.
     *
     * @param vehicleId the vehicle ID to set
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Gets the fuel type ID.
     *
     * @return fuel type ID
     */
    public int getFuelTypeId() {
        return fuelTypeId;
    }

    /**
     * Sets the fuel type ID.
     *
     * @param fuelTypeId the fuel type ID to set
     */
    public void setFuelTypeId(int fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    /**
     * Gets the amount of fuel used.
     *
     * @return fuel used
     */
    public float getFuelUsed() {
        return fuelUsed;
    }

    /**
     * Sets the amount of fuel used.
     *
     * @param fuelUsed the fuel usage value to set
     */
    public void setFuelUsed(float fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    /**
     * Gets the distance traveled.
     *
     * @return distance traveled
     */
    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Sets the distance traveled.
     *
     * @param distanceTraveled the distance value to set
     */
    public void setDistanceTraveled(float distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    /**
     * Gets the timestamp of the record.
     *
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the record.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Gets the consumption status (e.g., Normal, Warning, Critical).
     *
     * @return status string
     */
    public String getStatus() {
        return status;
    }


    /**
     * Sets the consumption status.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
