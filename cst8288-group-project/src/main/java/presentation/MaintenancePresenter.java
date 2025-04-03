package presentation;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenanceTaskManager;
import model.MaintenanceTask.ComponentType;
import model.MaintenanceTask.ComponentMonitoringParams;
import view.MaintenanceView;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    
    public void monitorComponent(ComponentMonitoringParams params) {
        String vehicleId = params.getVehicleId();
        Map<String, Double> measurements = params.getParameters();
        
        switch (params.getComponentType()) {
            case MECHANICAL:
                double brakeWear = measurements.getOrDefault("brakeWear", 0.0);
                double wheelWear = measurements.getOrDefault("wheelWear", 0.0);
                double bearingWear = measurements.getOrDefault("bearingWear", 0.0);
                monitor.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
                break;
                
            case ELECTRICAL:
                double catenaryWear = measurements.getOrDefault("catenaryWear", 0.0);
                double pantographWear = measurements.getOrDefault("pantographWear", 0.0);
                double breakerWear = measurements.getOrDefault("breakerWear", 0.0);
                monitor.monitorElectricalComponents(vehicleId, catenaryWear, pantographWear, breakerWear);
                break;
                
            case ENGINE:
                double engineTemp = measurements.getOrDefault("engineTemp", 0.0);
                double oilPressure = measurements.getOrDefault("oilPressure", 0.0);
                double fuelEfficiency = measurements.getOrDefault("fuelEfficiency", 0.0);
                monitor.monitorEngineDiagnostics(vehicleId, engineTemp, oilPressure, fuelEfficiency);
                break;
        }
    }
    
    // 使用示例：
    public void monitorMechanicalExample(String vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.MECHANICAL);
        params.addParameter("brakeWear", brakeWear);
        params.addParameter("wheelWear", wheelWear);
        params.addParameter("bearingWear", bearingWear);
        monitorComponent(params);
    }
    
    public void monitorElectricalExample(String vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ELECTRICAL);
        params.addParameter("catenaryWear", catenaryWear);
        params.addParameter("pantographWear", pantographWear);
        params.addParameter("breakerWear", breakerWear);
        monitorComponent(params);
    }
    
    public void monitorEngineExample(String vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ENGINE);
        params.addParameter("engineTemp", engineTemp);
        params.addParameter("oilPressure", oilPressure);
        params.addParameter("fuelEfficiency", fuelEfficiency);
        monitorComponent(params);
    }
    
    public void clearAlerts() {
        monitor.clearAlerts();
    }
} 