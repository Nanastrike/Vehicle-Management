/**
 * File: BusConsumptionStrategyTest.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 * Description: Unit test for BusConsumptionStrategy class.
 */

package Fuel_test;

import Fuel_strategy.BusConsumptionStrategy;
import model.VehicleManagement.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusConsumptionStrategyTest {

    @Test
    public void testCalculateConsumption() {
        // Arrange
        BusConsumptionStrategy strategy = new BusConsumptionStrategy();
        Vehicle bus = new Vehicle();
        bus.setConsumptionRate(20.0f); // 20 L/100km

        double distance = 50.0; // 50 km

        // Act
        double result = strategy.calculateConsumption(bus, distance);

        // Assert
        assertEquals(10.0, result, 0.001); // Expected 20 * 50 / 100 = 10 L
    }
}
