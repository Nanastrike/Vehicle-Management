/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_strategy;

import Fuel_model.Vehicle;


public class LightRailConsumptionStrategy implements ConsumptionStrategy {
    public double calculate(double rate, double distance) {
        return rate * distance;  // kWh/mile * distance
    }

    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}