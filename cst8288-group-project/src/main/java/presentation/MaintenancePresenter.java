package presentation;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenanceTaskManager;
import view.MaintenanceView;
import java.sql.SQLException;
import java.util.List;

public class MaintenancePresenter {
    private final MaintenanceView view;
    private final VehicleComponentMonitor monitor;
    private final MaintenanceTaskManager taskManager;
    
    public MaintenancePresenter(MaintenanceView view) throws SQLException {
        this.view = view;
        this.monitor = new VehicleComponentMonitor();
        this.taskManager = new MaintenanceTaskManager();
        this.monitor.addObserver(view);
    }
    
    public void displayMaintenanceTasks() throws SQLException {
        List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
        view.displayMaintenanceTasks(tasks);
    }
    
    public void displayComponentAlerts() {
        List<MaintenanceAlert> alerts = monitor.getComponentStatuses().stream()
            .map(MaintenanceAlert::new)
            .toList();
        view.displayComponentAlerts(alerts);
    }
    
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        monitor.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
    }
    
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        monitor.monitorElectricalComponents(vehicleId, catenaryWear, pantographWear, breakerWear);
    }
    
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        monitor.monitorEngineDiagnostics(vehicleId, engineTemp, oilPressure, fuelEfficiency);
    }
    
    public void clearAlerts() {
        monitor.clearAlerts();
    }
} 