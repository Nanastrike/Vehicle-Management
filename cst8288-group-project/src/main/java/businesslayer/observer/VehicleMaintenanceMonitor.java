package businesslayer.observer;

import java.util.ArrayList;
import java.util.List;

public class VehicleMaintenanceMonitor implements MaintenanceSubject {
    private static final double CRITICAL_WEAR_LEVEL = 80.0;
    private static final double WARNING_WEAR_LEVEL = 60.0;
    private static final double MAX_HOURS = 1000.0;
    private static final double CRITICAL_VOLTAGE = 1000.0;
    private static final double WARNING_VOLTAGE = 800.0;
    private static final double CRITICAL_CURRENT = 100.0;
    private static final double WARNING_CURRENT = 80.0;
    private static final double CRITICAL_TEMPERATURE = 100.0;
    private static final double WARNING_TEMPERATURE = 80.0;
    private static final double CRITICAL_PRESSURE = 200.0;
    private static final double WARNING_PRESSURE = 150.0;
    
    private List<MaintenanceObserver> observers;
    private String vehicleId;
    private String componentType;
    private double wearLevel;
    private String alertMessage;
    
    public VehicleMaintenanceMonitor(String vehicleId) {
        this.observers = new ArrayList<>();
        this.vehicleId = vehicleId;
    }
    
    @Override
    public void registerObserver(MaintenanceObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(MaintenanceObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (MaintenanceObserver observer : observers) {
            observer.update(vehicleId, componentType, wearLevel, alertMessage);
        }
    }
    
    public void setMaintenanceData(String componentType, double wearLevel, String alertMessage) {
        this.componentType = componentType;
        this.wearLevel = wearLevel;
        this.alertMessage = alertMessage;
        notifyObservers();
    }
    
    public void monitorMechanicalComponents(String componentType, double hoursOfUse) {
        double wearRate = hoursOfUse / MAX_HOURS;
        double wearLevel = Math.min(wearRate * 100, 100);
        
        String alertMessage;
        if (wearLevel >= CRITICAL_WEAR_LEVEL) {
            alertMessage = "Immediate maintenance required!";
        } else if (wearLevel >= WARNING_WEAR_LEVEL) {
            alertMessage = "Maintenance recommended";
        } else {
            alertMessage = "Operating normally";
        }
        
        setMaintenanceData(componentType, wearLevel, alertMessage);
    }
    
    public void monitorElectricalComponents(String componentType, double voltage, double current) {
        double wearLevel = 0;
        String alertMessage;
        
        if (voltage > CRITICAL_VOLTAGE || current > CRITICAL_CURRENT) {
            wearLevel = 90;
            alertMessage = "Electrical component anomaly detected, immediate inspection required!";
        } else if (voltage > WARNING_VOLTAGE || current > WARNING_CURRENT) {
            wearLevel = 70;
            alertMessage = "Electrical component inspection needed";
        } else {
            wearLevel = 30;
            alertMessage = "Electrical component operating normally";
        }
        
        setMaintenanceData(componentType, wearLevel, alertMessage);
    }
    
    public void monitorEngineDiagnostics(String componentType, double temperature, double pressure) {
        double wearLevel = 0;
        String alertMessage;
        
        if (temperature > CRITICAL_TEMPERATURE || pressure > CRITICAL_PRESSURE) {
            wearLevel = 95;
            alertMessage = "Engine temperature or pressure too high, immediate inspection required!";
        } else if (temperature > WARNING_TEMPERATURE || pressure > WARNING_PRESSURE) {
            wearLevel = 65;
            alertMessage = "Routine engine inspection needed";
        } else {
            wearLevel = 20;
            alertMessage = "Engine operating normally";
        }
        
        setMaintenanceData(componentType, wearLevel, alertMessage);
    }
} 