/**
 * File: AlertService.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class implements the Observer interface to act as an alerting mechanism for excessive fuel or energy consumption.
 * It is designed to notify a specified transit manager whenever the monitored value exceeds a defined threshold.
 * This is part of the observer pattern used in the fuel monitoring system.
 */

package Fuel_observer;

/**
 * AlertService is a concrete observer that receives notifications when consumption exceeds thresholds.
 * It simulates sending an alert to a manager by printing an alert message to the console.
 */
public class AlertService implements Observer {
    
    private String managerName; 

    /**
     * Constructs an AlertService assigned to a specific manager.
     *
     * @param managerName The name of the manager to notify.
     */
    public AlertService(String managerName) {
        this.managerName = managerName;
    }

    /**
     * Constructs an AlertService assigned to a specific manager.
     *
     * @param managerName The name of the manager to notify.
     */
    @Override
    public void update(double consumption) {
        System.out.println("Alert for " + managerName 
            + ": Consumption exceeded threshold! Current = " + consumption);
    }
}
