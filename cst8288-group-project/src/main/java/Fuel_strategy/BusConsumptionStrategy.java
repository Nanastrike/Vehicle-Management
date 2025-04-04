package Fuel_strategy;

import Fuel_model.Vehicle;

public class BusConsumptionStrategy implements ConsumptionStrategy {
    public double calculate(double rate, double distance) {
        return (rate / 100.0) * distance;  // L/100km * distance
    }

    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}