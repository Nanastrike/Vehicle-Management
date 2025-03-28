package businesslayer.observer;

import model.MaintenanceAlert;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceAlertObserver implements MaintenanceObserver {
    private String observerName;
    private List<MaintenanceAlert> alerts;
    
    public MaintenanceAlertObserver(String name) {
        this.observerName = name;
        this.alerts = new ArrayList<>();
    }
    
    @Override
    public void update(String vehicleId, String componentType, double wearLevel, String alertMessage) {
        MaintenanceAlert alert = new MaintenanceAlert(vehicleId, componentType, wearLevel, alertMessage);
        alerts.add(alert);
        
        System.out.println("[" + observerName + "] Maintenance Alert Received:");
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Component Type: " + componentType);
        System.out.println("Wear Level: " + wearLevel + "%");
        System.out.println("Alert Message: " + alertMessage);
        System.out.println("------------------------");
    }
    
    public List<MaintenanceAlert> getAlerts() {
        return alerts;
    }
    
    public void clearAlerts() {
        alerts.clear();
    }
} 