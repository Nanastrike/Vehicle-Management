/**
 * File: FuelServiceTest.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 * Description: Unit test for LightRailConsumptionStrategy class.
 */
package Fuel_test;

import Fuel_strategy.LightRailConsumptionStrategy;
import model.VehicleManagement.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LightRailConsumptionStrategyTest {

    @Test
    public void testCalculateConsumption() {
        Vehicle vehicle = new Vehicle();
        vehicle.setElectricRate(1.5); 
        double distance = 10.0;

        LightRailConsumptionStrategy strategy = new LightRailConsumptionStrategy();
        double expected = 1.5 * 10.0;
        double actual = strategy.calculateConsumption(vehicle, distance);

        assertEquals(expected, actual, 0.001, "Electric consumption should match expected value");
    }
}
