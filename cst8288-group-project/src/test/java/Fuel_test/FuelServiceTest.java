/**
 * File: FuelServiceTest.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 * Description: Unit test for FuelService class.
 */

package Fuel_test;

import Fuel_service.FuelService;
import Fuel_observer.ConsumptionMonitor;
import model.VehicleManagement.Vehicle;
import model.VehicleManagement.VehicleType;
import model.VehicleManagement.FuelType;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FuelServiceTest {

    @Test
    public void testCalculateFuelForDieselBus() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setConsumptionRate(25.0f); // 25 L/100km
        vehicle.setVehicleType(new VehicleType(1, "Diesel Bus"));
        vehicle.setFuelType(new FuelType(1, "Diesel"));

        ConsumptionMonitor monitor = new ConsumptionMonitor(50.0);
        FuelService service = new FuelService(monitor);

        double distance = 100.0;

        // Act
        double result = service.calculateFuel(vehicle, distance);

        // Assert: expected = 25 * 100 / 100 = 25
        assertEquals(25.0, result, 0.001);
    }
}
