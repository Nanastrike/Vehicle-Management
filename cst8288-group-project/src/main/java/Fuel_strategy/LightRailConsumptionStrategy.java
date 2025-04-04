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
        return electricRate * distance;
    }
}
