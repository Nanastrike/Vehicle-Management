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
    
    /**
     * Monitors mechanical components of a vehicle by tracking wear levels.
     * Creates and processes monitoring parameters for mechanical components
     * such as brakes, wheels, and bearings.
     *
     * @param vehicleId The ID of the vehicle to monitor
     * @param brakeWear The wear level of the brakes (percentage)
     * @param wheelWear The wear level of the wheels (percentage)
     * @param bearingWear The wear level of the bearings (percentage)
     */
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, 
                                          double wheelWear, double bearingWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.MECHANICAL);
        params.addParameter("brakeWear", brakeWear);
        params.addParameter("wheelWear", wheelWear);
        params.addParameter("bearingWear", bearingWear);
        monitorComponent(params);
    }
    
    /**
     * Monitors electrical components of a vehicle by tracking wear levels.
     * Creates and processes monitoring parameters for electrical components
     * such as catenary, pantograph, and circuit breakers.
     *
     * @param vehicleId The ID of the vehicle to monitor
     * @param catenaryWear The wear level of the catenary system (percentage)
     * @param pantographWear The wear level of the pantograph (percentage)
     * @param breakerWear The wear level of the circuit breakers (percentage)
     */
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, 
                                          double pantographWear, double breakerWear) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ELECTRICAL);
        params.addParameter("catenaryWear", catenaryWear);
        params.addParameter("pantographWear", pantographWear);
        params.addParameter("breakerWear", breakerWear);
        monitorComponent(params);
    }
    
    /**
     * Monitors engine diagnostics by tracking various engine parameters.
     * Creates and processes monitoring parameters for engine components
     * including temperature, oil pressure, and fuel efficiency.
     *
     * @param vehicleId The ID of the vehicle to monitor
     * @param engineTemp The engine temperature (degrees Celsius)
     * @param oilPressure The oil pressure (PSI)
     * @param fuelEfficiency The fuel efficiency (km/L)
     */
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, 
                                        double oilPressure, double fuelEfficiency) {
        ComponentMonitoringParams params = new ComponentMonitoringParams(vehicleId, ComponentType.ENGINE);
        params.addParameter("engineTemp", engineTemp);
        params.addParameter("oilPressure", oilPressure);
        params.addParameter("fuelEfficiency", fuelEfficiency);
        monitorComponent(params);
    }

    /**
     * Processes component monitoring parameters and updates the component status.
     * Calculates average parameter values and creates a new component status
     * for monitoring.
     *
     * @param params The monitoring parameters to process
     */
    private void monitorComponent(ComponentMonitoringParams params) {
        // Create and update component status based on monitoring parameters
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