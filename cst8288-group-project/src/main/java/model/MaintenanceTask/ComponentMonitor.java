package model.MaintenanceTask;

import java.util.List;

public interface ComponentMonitor {
    void addObserver(ComponentObserver observer);
    void removeObserver(ComponentObserver observer);
    void notifyObservers();
    List<ComponentStatus> getComponentStatuses();
} 