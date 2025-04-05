/**
 * File: Observer.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This interface defines the Observer in the Observer design pattern.
 * Classes that implement this interface should override the update() method
 * to react to state changes in the Subject (e.g., ConsumptionMonitor).
 */

package Fuel_observer;

/**
 * Observer interface for receiving updates from a Subject when state changes occur.
 * Typically used in monitoring scenarios such as fuel or energy consumption tracking.
 */
public interface Observer {
    /**
     * Invoked when the Subject's state changes and the observer needs to respond.
     *
     * @param consumption The current consumption value that triggered the update.
     */
    void update(double consumption);
}

