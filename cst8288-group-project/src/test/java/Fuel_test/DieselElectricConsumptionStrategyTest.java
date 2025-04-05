/**
 * File: FuelServiceTest.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 * Description: Unit test for DieselElectricConsumptionStrategy class.
 */
package Fuel_test;

import Fuel_strategy.DieselElectricConsumptionStrategy;
import model.VehicleManagement.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DieselElectricConsumptionStrategyTest {

    @Test
    public void testCalculateConsumption() {
        Vehicle vehicle = new Vehicle();
        vehicle.setDieselRate(5.0);     // 5L / 100km
        vehicle.setElectricRate(1.2);   // 1.2 kWh / km
        double distance = 50.0;

        DieselElectricConsumptionStrategy strategy = new DieselElectricConsumptionStrategy();
        double expected = (5.0 * 50.0 / 100.0) + (1.2 * 50.0);
        double actual = strategy.calculateConsumption(vehicle, distance);

        assertEquals(expected, actual, 0.001, "Total combined consumption should match expected value");
    }
}
