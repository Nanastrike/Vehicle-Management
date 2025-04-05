/**
 * File: BusConsumptionStrategy.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class implements the ConsumptionStrategy interface to provide
 * a specific calculation method for diesel or CNG buses.
 * It uses the vehicle's consumption rate (liters per 100 km)
 * to compute the total fuel used for a given distance.
 */
package Fuel_strategy;

import model.VehicleManagement.Vehicle;

/**
 * Strategy for calculating fuel consumption for traditional buses.
 * Applies to diesel or CNG-powered buses using a per-100km rate.
 */
public class BusConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates fuel consumption for a bus over a given distance.
     *
     * @param vehicle The bus vehicle with a defined consumption rate
     * @param distance The distance traveled in kilometers
     * @return The total fuel consumed in liters
     */
    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
      
        double rate = vehicle.getConsumptionRate(); 
        double consumption = rate * distance / 100.0;
        return consumption;
    }
}
