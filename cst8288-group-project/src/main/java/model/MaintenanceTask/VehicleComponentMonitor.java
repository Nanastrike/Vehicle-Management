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
    
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        // 監控機械組件
        if (brakeWear > 80) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Brakes", brakeWear, "CRITICAL",
                "Brake wear level critical - immediate maintenance required"
            ));
        }
        
        if (wheelWear > 75) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Wheels", wheelWear, "WARNING",
                "Wheel wear level high - maintenance needed soon"
            ));
        }
        
        if (bearingWear > 70) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Bearings", bearingWear, "WARNING",
                "Bearing wear level high - maintenance needed soon"
            ));
        }
    }
    
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        // 監控電氣組件
        if (catenaryWear > 85) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Catenary", catenaryWear, "CRITICAL",
                "Catenary wear level critical - immediate maintenance required"
            ));
        }
        
        if (pantographWear > 80) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Pantograph", pantographWear, "WARNING",
                "Pantograph wear level high - maintenance needed soon"
            ));
        }
        
        if (breakerWear > 75) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Circuit Breaker", breakerWear, "WARNING",
                "Circuit breaker wear level high - maintenance needed soon"
            ));
        }
    }
    
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        // 監控引擎診斷
        if (engineTemp > 90) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Engine Temperature", engineTemp, "CRITICAL",
                "Engine temperature critical - immediate maintenance required"
            ));
        }
        
        if (oilPressure < 20) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Oil Pressure", oilPressure, "WARNING",
                "Oil pressure low - maintenance needed soon"
            ));
        }
        
        if (fuelEfficiency < 70) {
            updateComponentStatus(new ComponentStatus(
                vehicleId, "Fuel Efficiency", fuelEfficiency, "WARNING",
                "Fuel efficiency low - maintenance needed soon"
            ));
        }
    }
} 