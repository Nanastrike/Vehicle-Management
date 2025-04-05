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

/**
 * MaintenancePresenter serves as an intermediary between the maintenance view and model components.
 * This class implements the Presenter role in the MVP (Model-View-Presenter) architecture pattern,
 * handling all business logic related to vehicle maintenance operations and coordinating data flow
 * between the view and various model components.
 * 
 * <p>The presenter manages:</p>
 * <ul>
 *     <li>Vehicle component monitoring</li>
 *     <li>Maintenance task scheduling and management</li>
 *     <li>Vehicle data access</li>
 *     <li>Alert notifications through the observer pattern</li>
 * </ul>
 * 
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since 1.21
 * 
 * @see MaintenanceView
 * @see VehicleComponentMonitor
 * @see MaintenanceTaskManager
 * @see VehicleDAO
 */

public class MaintenancePresenter {
    private final MaintenanceView view;
    private final VehicleComponentMonitor monitor;
    private final MaintenanceTaskManager taskManager;
    private final VehicleDAO vehicleDAO;
    
    /**
     * Initializes the presenter with the given view and other dependencies.
     * 
     * @param view The view component to interact with
     * @throws SQLException if a database connection error occurs
     */
    public MaintenancePresenter(MaintenanceView view) throws SQLException {
        this.view = view;
        this.monitor = new VehicleComponentMonitor();
        this.taskManager = new MaintenanceTaskManager();
        this.vehicleDAO = new VehicleDAO(DatabaseConnection.getInstance().getConnection());
        this.monitor.addObserver(view);
    }
    
    /**
     * Displays all maintenance tasks in the system.
     * 
     * @throws SQLException if a database connection error occurs
     */
    public void displayMaintenanceTasks() throws SQLException {
        List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
        view.displayMaintenanceTasks(tasks);
    }
    
    /**
     * Displays component alerts for the current vehicle.
     */
    public void displayComponentAlerts() {
        List<MaintenanceAlert> alerts = monitor.getComponentStatuses().stream()
            .map(MaintenanceAlert::new)
            .toList();
        view.displayComponentAlerts(alerts);
    }

    /**
     * Retrieves vehicles that need maintenance based on the last maintenance date.
     * 
     * @return List of vehicles needing maintenance
     * @throws SQLException if a database connection error occurs
     */
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

    /**
     * Retrieves all vehicles from the vehicle data access object.
     * 
     * @return List of all vehicles
     * @throws SQLException if a database connection error occurs
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        return vehicleDAO.getAllVehicles();
    }

    /**
     * Schedules a new maintenance task for a vehicle.
     * 
     * @param vehicleNumber The vehicle number to schedule the task for
     * @param taskType The type of maintenance task to schedule
     * @param scheduledDate The date and time of the scheduled task
     * @param priority The priority level of the task
     * @throws SQLException if a database connection error occurs
     */
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

    /**
     * Deletes a maintenance task from the task manager.
     * 
     * @param taskId The ID of the task to delete
     * @throws SQLException if a database connection error occurs
     */
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
    
    /**
     * Clears all alerts from the vehicle component monitor.
     */
    public void clearAlerts() {
        monitor.clearAlerts();
    }
} 