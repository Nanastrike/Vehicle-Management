package model.MaintenanceTask;

import java.util.List;

/**
 * Interface defining the contract for component monitoring functionality.
 * Implements the Observer pattern to notify interested parties of component status changes.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentObserver
 * @see ComponentStatus
 */
public interface ComponentMonitor {
    /**
     * Adds an observer to be notified of component status changes.
     * @param observer The observer to add
     */
    void addObserver(ComponentObserver observer);

    /**
     * Removes an observer from the notification list.
     * @param observer The observer to remove
     */
    void removeObserver(ComponentObserver observer);

    /**
     * Notifies all registered observers of component status changes.
     */
    void notifyObservers();

    /**
     * Retrieves the current status of all monitored components.
     * @return List of component status objects
     */
    List<ComponentStatus> getComponentStatuses();
} 