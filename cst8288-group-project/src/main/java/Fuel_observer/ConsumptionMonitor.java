/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fuel_observer;

/**
 *
 * @author xiaox
 */
import java.util.ArrayList;
import java.util.List;

public class ConsumptionMonitor implements Subject {
    private List<Observer> observers;
    private double threshold;
    private double currentConsumption;

    public ConsumptionMonitor(double threshold) {
        this.observers = new ArrayList<>();
        this.threshold = threshold;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(currentConsumption);
        }
    }

    public void setConsumption(double consumption) {
        this.currentConsumption = consumption;
        // If consumption > threshold => alert
        if (consumption > threshold) {
            notifyObservers();
        }
    }
}

