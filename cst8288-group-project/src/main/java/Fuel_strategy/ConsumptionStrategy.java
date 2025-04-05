/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_strategy;

import model.VehicleManagement.Vehicle;

/**
 *
 * @author xiaox
 */
public interface ConsumptionStrategy {
    /**
 * Strategy interface for calculating fuel or energy consumption.
 * Implementations define how consumption is calculated based on vehicle type and distance.
 * 
 * @param vehicle The vehicle for which consumption is to be calculated.
 * @param distance The distance traveled in kilometers.
 * @return The calculated amount of fuel or energy consumed.
 */
    double calculateConsumption(Vehicle vehicle, double distance);
}

