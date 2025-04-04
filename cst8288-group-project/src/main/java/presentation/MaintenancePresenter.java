package presentation;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenanceTaskManager;
import model.MaintenanceTask.ComponentType;
import model.MaintenanceTask.ComponentMonitoringParams;
import model.VehicleManagement.Vehicle;
import view.MaintenanceView;
import data.VehicleDAO;
import data.DatabaseConnection;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class MaintenancePresenter {
    private final MaintenanceView view;
    private final VehicleComponentMonitor monitor;
    private final MaintenanceTaskManager taskManager;
    private final VehicleDAO vehicleDAO;
    
    public MaintenancePresenter(MaintenanceView view) throws SQLException {
        this.view = view;
        this.monitor = new VehicleComponentMonitor();
        this.taskManager = new MaintenanceTaskManager();
        this.vehicleDAO = new VehicleDAO(DatabaseConnection.getInstance().getConnection());
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

    public List<Vehicle> getVehiclesNeedingMaintenance() throws SQLException {
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        List<Vehicle> vehiclesNeedingMaintenance = new ArrayList<>();
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getLastMaintenanceDate() != null) {
                LocalDateTime lastMaintenance = vehicle.getLastMaintenanceDate().toLocalDate().atStartOfDay();
                if (lastMaintenance.isBefore(threeMonthsAgo)) {
                    vehiclesNeedingMaintenance.add(vehicle);
                }
            }
        }
        
        return vehiclesNeedingMaintenance;
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        return vehicleDAO.getAllVehicles();
    }

    public void scheduleMaintenanceTask(String vehicleNumber, String taskType, 
                                      LocalDateTime scheduledDate, String priority) throws SQLException {
        taskManager.createMaintenanceTask(
            Integer.parseInt(vehicleNumber),
            taskType,
            scheduledDate,
            "System",
            priority
        );
    }

    public void deleteMaintenanceTask(int taskId) throws SQLException {
        taskManager.deleteMaintenanceTask(taskId);
    }
    
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, 
                                          double wheelWear, double bearingWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.MECHANICAL);
        params.addParameter("brakeWear", brakeWear);
        params.addParameter("wheelWear", wheelWear);
        params.addParameter("bearingWear", bearingWear);
        monitorComponent(params);
    }
    
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, 
                                          double pantographWear, double breakerWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ELECTRICAL);
        params.addParameter("catenaryWear", catenaryWear);
        params.addParameter("pantographWear", pantographWear);
        params.addParameter("breakerWear", breakerWear);
        monitorComponent(params);
    }
    
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, 
                                        double oilPressure, double fuelEfficiency) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ENGINE);
        params.addParameter("engineTemp", engineTemp);
        params.addParameter("oilPressure", oilPressure);
        params.addParameter("fuelEfficiency", fuelEfficiency);
        monitorComponent(params);
    }

    private void monitorComponent(ComponentMonitoringParams params) {
        // Implementation of component monitoring logic
        ComponentStatus status = new ComponentStatus(
            params.getVehicleId(),
            params.getComponentType().getDisplayName(),
            0, // hoursUsed - could be calculated based on maintenance history
            params.getParameters().values().stream()
                .mapToDouble(v -> (double) v)
                .average()
                .orElse(0.0)
        );
        monitor.updateComponentStatus(status);
    }
    
    public void clearAlerts() {
        monitor.clearAlerts();
    }
} 