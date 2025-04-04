/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_strategy;

import Fuel_model.Vehicle;

public class DieselElectricConsumptionStrategy implements ConsumptionStrategy {

    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        
        double dieselRate = vehicle.getDieselRate();      // L/100km
        double electricRate = vehicle.getElectricRate();  // kWh/mile

       
        double dieselConsumption = dieselRate * distance / 100.0;
        double electricConsumption = electricRate * distance;

        return dieselConsumption + electricConsumption;
    }
}

