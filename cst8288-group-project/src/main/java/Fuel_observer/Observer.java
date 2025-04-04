/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_observer;

/**
 *
 * @author xiaox
 */
public interface Observer {
    /**
     * Called when the Subject (ConsumptionMonitor) detects consumption
     * exceeds threshold.
     */
    void update(double consumption);
}

