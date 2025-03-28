package businesslayer.observer;

public interface MaintenanceObserver {
    void update(String vehicleId, String componentType, double wearLevel, String alertMessage);
} 