/**
 * File: FuelService.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This service class handles fuel or energy consumption calculation 
 * using the Strategy Design Pattern. It dynamically selects an appropriate 
 * strategy based on the vehicle type and invokes the calculation logic.
 * It also interacts with the Observer (ConsumptionMonitor) to notify 
 * registered observers when fuel consumption exceeds a defined threshold.
 */
package Fuel_service;

import model.VehicleManagement.Vehicle;
import Fuel_observer.ConsumptionMonitor;
import Fuel_strategy.BusConsumptionStrategy;
import Fuel_strategy.ConsumptionStrategy;
import Fuel_strategy.DieselElectricConsumptionStrategy;
import Fuel_strategy.LightRailConsumptionStrategy;

/**
 * Service responsible for calculating fuel or energy consumption
 * using the Strategy design pattern based on vehicle type.
 * Also notifies observers (e.g., AlertService) if consumption exceeds a threshold.
 */
public class FuelService {

    private final ConsumptionMonitor monitor;
    
    /**
     * Constructs a FuelService and registers it with the provided monitor.
     *
     * @param monitor the ConsumptionMonitor used for alert triggering
     */
    public FuelService(ConsumptionMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Calculates consumption based on the vehicle type and distance.
     * Automatically selects the appropriate strategy and notifies the monitor.
     *
     * @param vehicle The vehicle object containing all configuration data
     * @param distance The distance traveled in kilometers
     * @return The calculated fuel or energy usage
     */
    public double calculateFuel(Vehicle vehicle, double distance) {
        // Strategy selection based on vehicle type
        ConsumptionStrategy strategy = selectStrategy(vehicle);

        // Perform calculation
        double consumption = strategy.calculateConsumption(vehicle, distance);

        // Notify the monitor for alert purposes
        monitor.setConsumption(consumption);

        return consumption;
    }

    /**
     * Selects the correct ConsumptionStrategy implementation
     * based on vehicle type (e.g., Bus, Light Rail, Diesel-Electric).
     *
     * @param vehicle The vehicle object
     * @return An instance of a class that implements ConsumptionStrategy
     */
    private ConsumptionStrategy selectStrategy(Vehicle vehicle) {
        String type = vehicle.getVehicleType().getTypeName();

        switch (type) {
            case "Diesel Bus":
            case "CNG Bus":  // Optional: handle future expansion
                return new BusConsumptionStrategy();

            case "Electric Light Rail":
                return new LightRailConsumptionStrategy();

            case "Diesel-Electric Train":
                return new DieselElectricConsumptionStrategy();

            default:
                throw new UnsupportedOperationException("Unsupported vehicle type: " + type);
        }
    }
}
