/**
 * File: LightRailConsumptionStrategy.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class implements the ConsumptionStrategy interface and provides
 * logic to calculate the electric energy consumption of light rail vehicles.
 * It assumes the vehicle uses only electric energy, measured in kilowatt-hours
 * per kilometer (kWh/km).
 */
package Fuel_strategy;

import model.VehicleManagement.Vehicle;

/**
 * Strategy for calculating energy consumption for Light Rail vehicles.
 * Assumes electric-only consumption measured in kWh/km.
 */
public class LightRailConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates electric energy consumption.
     *
     * @param vehicle the light rail vehicle object
     * @param distance the distance traveled in kilometers
     * @return total electricity consumption in kWh
     */
    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        double electricRate = vehicle.getElectricRate();  // in kWh/km
        if (electricRate == 0.0) {
        electricRate = 1.5;  // Default fallback value
        }
        return electricRate * distance;
    }
}
