package model.MaintenanceTask;

import java.util.ArrayList;
import java.util.List;

public class VehicleComponentMonitor implements ComponentMonitor {
    private List<ComponentObserver> observers;
    private List<ComponentStatus> componentStatuses;
    
    public VehicleComponentMonitor() {
        this.observers = new ArrayList<>();
        this.componentStatuses = new ArrayList<>();
    }
    
    @Override
    public void addObserver(ComponentObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(ComponentObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (ComponentObserver observer : observers) {
            observer.update(componentStatuses);
        }
    }
    
    @Override
    public List<ComponentStatus> getComponentStatuses() {
        return componentStatuses;
    }
    
    public void updateComponentStatus(ComponentStatus status) {
        componentStatuses.add(status);
        notifyObservers();
    }
    
    public void clearAlerts() {
        componentStatuses.clear();
        notifyObservers();
    }
    
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        // 監控機械組件
        if (brakeWear > 80) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Brakes", 0, brakeWear
            ));
        }
        
        if (wheelWear > 75) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Wheels", 0, wheelWear
            ));
        }
        
        if (bearingWear > 70) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Bearings", 0, bearingWear
            ));
        }
    }
    
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        // 監控電氣組件
        if (catenaryWear > 85) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Catenary", 0, catenaryWear
            ));
        }
        
        if (pantographWear > 80) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Pantograph", 0, pantographWear
            ));
        }
        
        if (breakerWear > 75) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Circuit Breaker", 0, breakerWear
            ));
        }
    }
    
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        // 監控引擎診斷
        if (engineTemp > 90) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Engine Temperature", 0, engineTemp
            ));
        }
        
        if (oilPressure < 20) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Oil Pressure", 0, oilPressure
            ));
        }
        
        if (fuelEfficiency < 70) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Fuel Efficiency", 0, fuelEfficiency
            ));
        }
    }
    
    public void checkMechanicalComponents(int vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.add(new ComponentStatus("Brakes", brakeWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Wheels/Tires", wheelWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Axle Bearings", bearingWear, "Hours", vehicleId));
        
        for (ComponentObserver observer : observers) {
            observer.update(statuses);
        }
    }
    
    public void checkElectricalComponents(int vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.add(new ComponentStatus("Catenary", catenaryWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Pantograph", pantographWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Circuit Breaker", breakerWear, "Hours", vehicleId));
        
        for (ComponentObserver observer : observers) {
            observer.update(statuses);
        }
    }
    
    public void checkEngineDiagnostics(int vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.add(new ComponentStatus("Engine Temperature", engineTemp, "°C", vehicleId));
        statuses.add(new ComponentStatus("Oil Pressure", oilPressure, "PSI", vehicleId));
        statuses.add(new ComponentStatus("Fuel Efficiency", fuelEfficiency, "km/L", vehicleId));
        
        for (ComponentObserver observer : observers) {
            observer.update(statuses);
        }
    }
} 