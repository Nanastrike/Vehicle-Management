/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_service;

import model.VehicleManagement.Vehicle;
import Fuel_observer.ConsumptionMonitor;
import Fuel_strategy.BusConsumptionStrategy;
import Fuel_strategy.ConsumptionStrategy;
import Fuel_strategy.DieselElectricConsumptionStrategy;
import Fuel_strategy.LightRailConsumptionStrategy;

/**
 *
 * @author xiaox
 */
public class FuelService {
    private final ConsumptionMonitor monitor;

    public FuelService(ConsumptionMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Main method to calculate consumption for a given vehicle.This method automatically selects a strategy based on vehicleType,
 calculates consumption, and updates the monitor.
     * @param vehicle
     * @param distance
     * @return 
     */
    public double calculateFuel(Vehicle vehicle, double distance) {
        ConsumptionStrategy strategy = selectStrategy(vehicle);
        double consumption = strategy.calculateConsumption(vehicle, distance);

        // Update monitor so that if it exceeds threshold, an alert is triggered
        monitor.setConsumption(consumption);

        return consumption;
    }

    private ConsumptionStrategy selectStrategy(Vehicle vehicle) {
        String type = vehicle.getVehicleType().getTypeName(); 
        
        switch (type) {
            case "Diesel Bus":
                return new BusConsumptionStrategy();
            case "Electric Light Rail":
                return new LightRailConsumptionStrategy();
            case "Diesel-Electric Train":
                return new DieselElectricConsumptionStrategy();
            default:
                throw new UnsupportedOperationException("Unknown vehicle type: " + type);
        }
    }
}
