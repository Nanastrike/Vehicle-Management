package Fuel_strategy;

import model.VehicleManagement.Vehicle;

public class BusConsumptionStrategy implements ConsumptionStrategy {

    @Override
    public double calculateConsumption(Vehicle vehicle, double distance) {
        // 使用 consumptionRate 作为 L/100km 的油耗
        double rate = vehicle.getConsumptionRate(); // 通常是 float，也可以强转 double
        double consumption = rate * distance / 100.0;
        return consumption;
    }
}
