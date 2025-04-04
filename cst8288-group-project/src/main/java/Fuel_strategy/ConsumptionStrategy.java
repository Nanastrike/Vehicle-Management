/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_strategy;

import Fuel_model.Vehicle;

/**
 *
 * @author xiaox
 */
public interface ConsumptionStrategy {
    /**
     * Calculates the total fuel/energy consumption for a given vehicle over a certain distance.
     * @param vehicle
     * @param distance
     * @return 
     */
    double calculateConsumption(Vehicle vehicle, double distance);
}

