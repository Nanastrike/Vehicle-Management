package Fuel_strategy;

import model.VehicleManagement.Vehicle;

public class BusConsumptionStrategy implements ConsumptionStrategy {

    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
      
        double rate = vehicle.getConsumptionRate(); 
        double consumption = rate * distance / 100.0;
        return consumption;
    }
}
