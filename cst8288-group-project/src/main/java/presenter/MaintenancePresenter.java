package presenter;

import businesslayer.observer.MaintenanceAlertObserver;
import businesslayer.observer.VehicleMaintenanceMonitor;
import model.MaintenanceAlert;
import java.util.List;

public class MaintenancePresenter {
    private VehicleMaintenanceMonitor monitor;
    private MaintenanceAlertObserver observer;
    
    public MaintenancePresenter(String vehicleId) {
        this.monitor = new VehicleMaintenanceMonitor(vehicleId);
        this.observer = new MaintenanceAlertObserver("系統監控器");
        this.monitor.registerObserver(observer);
    }
    
    public void checkMechanicalComponents(String componentType, double hoursOfUse) {
        monitor.monitorMechanicalComponents(componentType, hoursOfUse);
    }
    
    public void checkElectricalComponents(String componentType, double voltage, double current) {
        monitor.monitorElectricalComponents(componentType, voltage, current);
    }
    
    public void checkEngineDiagnostics(String componentType, double temperature, double pressure) {
        monitor.monitorEngineDiagnostics(componentType, temperature, pressure);
    }
    
    public List<MaintenanceAlert> getAlerts() {
        return observer.getAlerts();
    }
    
    public void clearAlerts() {
        observer.clearAlerts();
    }
} 