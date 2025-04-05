package model.MaintenanceTask;

import java.util.List;

/**
 * Interface for observers that want to be notified of component status changes.
 * Part of the Observer pattern implementation for component monitoring.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentMonitor
 * @see ComponentStatus
 */
public interface ComponentObserver {
    /**
     * Called when component statuses change in the monitored system.
     * @param statuses List of updated component status objects
     */
    void update(List<ComponentStatus> statuses);
} 