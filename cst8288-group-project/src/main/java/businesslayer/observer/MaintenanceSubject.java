package businesslayer.observer;

public interface MaintenanceSubject {
    void registerObserver(MaintenanceObserver observer);
    void removeObserver(MaintenanceObserver observer);
    void notifyObservers();
} 