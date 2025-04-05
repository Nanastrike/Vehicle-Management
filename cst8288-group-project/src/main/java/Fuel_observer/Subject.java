/**
 * File: Subject.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This interface defines the Subject in the Observer design pattern.
 * A Subject maintains a list of Observers and notifies them of any state changes.
 */
package Fuel_observer;

/**
 * Subject interface for implementing the Observer design pattern.
 * Represents the entity being monitored and allows observers to register or unregister
 * themselves to receive updates when relevant state changes occur.
 */
public interface Subject {
    
    /**
     * Registers an observer to be notified of state changes.
     *
     * @param observer the Observer to attach.
     */
    void attach(Observer observer);
    
    /**
     * Unregisters an observer from receiving future notifications.
     *
     * @param observer the Observer to detach.
     */
    void detach(Observer observer);
    
    /**
     * Notifies all registered observers of a state change.
     */
    void notifyObservers();
}

