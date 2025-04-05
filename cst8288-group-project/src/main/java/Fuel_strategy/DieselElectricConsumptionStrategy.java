/**
 * File: DieselElectricConsumptionStrategy.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class implements the ConsumptionStrategy interface to calculate
 * the fuel and electric energy usage of a diesel-electric hybrid vehicle.
 * It combines diesel consumption in liters and electric consumption in kilowatt-hours
 * based on the vehicle's configured rates.
 */
package Fuel_strategy;

import model.VehicleManagement.Vehicle;

/**
 * Strategy for calculating fuel consumption of diesel-electric trains.
 * It combines both diesel and electric energy usage.
 */
public class DieselElectricConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates the total energy consumption of a diesel-electric vehicle.
     *
     * @param vehicle the vehicle to calculate for
     * @param distance the distance traveled in kilometers
     * @return the total combined energy consumption
     */
    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        double dieselRate = vehicle.getDieselRate();       // in L/100km
        double electricRate = vehicle.getElectricRate();   // in kWh/km

        // Diesel consumption (L)
        double dieselConsumption = dieselRate * distance / 100.0;

        // Electric consumption (kWh)
        double electricConsumption = electricRate * distance;

        return dieselConsumption + electricConsumption;
    }
}
