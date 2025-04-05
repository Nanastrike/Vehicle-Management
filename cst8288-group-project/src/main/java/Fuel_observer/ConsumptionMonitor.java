/**
 * File: ConsumptionMonitor.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This class implements the Subject interface in the Observer design pattern.
 * It tracks fuel or energy consumption and notifies attached observers
 * (such as alerting services) when the consumption exceeds a defined threshold.
 */

package Fuel_observer;

import java.util.ArrayList;
import java.util.List;

/**
 * ConsumptionMonitor monitors consumption values and notifies observers when a threshold is exceeded.
 * It serves as the Subject in the Observer design pattern.
 */
public class ConsumptionMonitor implements Subject {
    private List<Observer> observers;
    private double threshold;
    private double currentConsumption;

    /**
     * Constructs a ConsumptionMonitor with a specific threshold value.
     *
     * @param threshold The consumption threshold for triggering notifications.
     */
    public ConsumptionMonitor(double threshold) {
        this.observers = new ArrayList<>();
        this.threshold = threshold;
    }

    /**
     * Attaches an observer to the list of listeners.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Detaches an observer from the list of listeners.
     *
     * @param observer The observer to be removed.
     */
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all attached observers with the current consumption value.
     */
    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(currentConsumption);
        }
    }

    /**
     * Sets the current consumption and triggers observer notifications if the value exceeds the threshold.
     *
     * @param consumption The new consumption value.
     */
    public void setConsumption(double consumption) {
        this.currentConsumption = consumption;
        // If consumption > threshold => alert
        if (consumption > threshold) {
            notifyObservers();
        }
    }
}

