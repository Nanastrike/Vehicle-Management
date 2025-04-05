package model.MaintenanceTask;

import java.util.ArrayList;
import java.util.List;

/**
 * VehicleComponentMonitor implements the ComponentMonitor interface to observe and track
 * the wear and performance status of various vehicle components.
 * 
 * <p>This class supports monitoring of mechanical, electrical, and engine-related components.
 * It notifies registered observers whenever a new component status is updated.</p>
 * 
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 */
public class VehicleComponentMonitor implements ComponentMonitor {
    private List<ComponentObserver> observers;
    private List<ComponentStatus> componentStatuses;

    /**
     * Initializes the monitor with empty observer and component status lists.
     */
    public VehicleComponentMonitor() {
        this.observers = new ArrayList<>();
        this.componentStatuses = new ArrayList<>();
    }

    /**
     * Registers a new component observer.
     *
     * @param observer the observer to add
     */
    @Override
    public void addObserver(ComponentObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an existing component observer.
     *
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver(ComponentObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers with the latest component statuses.
     */
    @Override
    public void notifyObservers() {
        for (ComponentObserver observer : observers) {
            observer.update(componentStatuses);
        }
    }

    /**
     * Returns the current list of component statuses.
     *
     * @return list of ComponentStatus objects
     */
    @Override
    public List<ComponentStatus> getComponentStatuses() {
        return componentStatuses;
    }

    /**
     * Updates the component status and notifies observers.
     *
     * @param status the updated component status
     */
    public void updateComponentStatus(ComponentStatus status) {
        componentStatuses.add(status);
        notifyObservers();
    }

    /**
     * Clears all current alerts and notifies observers.
     */
    public void clearAlerts() {
        componentStatuses.clear();
        notifyObservers();
    }

    /**
     * Monitors mechanical components for wear levels and generates alerts
     * if thresholds are exceeded.
     *
     * @param vehicleId vehicle identifier
     * @param brakeWear wear level of brakes
     * @param wheelWear wear level of wheels
     * @param bearingWear wear level of axle bearings
     */
    public void monitorMechanicalComponents(String vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        if (brakeWear > 80) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Brakes", 0, brakeWear));
        }
        if (wheelWear > 75) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Wheels", 0, wheelWear));
        }
        if (bearingWear > 70) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Bearings", 0, bearingWear));
        }
    }

    /**
     * Monitors electrical components and generates alerts based on wear thresholds.
     *
     * @param vehicleId vehicle identifier
     * @param catenaryWear wear level of catenary
     * @param pantographWear wear level of pantograph
     * @param breakerWear wear level of circuit breaker
     */
    public void monitorElectricalComponents(String vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        if (catenaryWear > 85) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Catenary", 0, catenaryWear));
        }
        if (pantographWear > 80) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Pantograph", 0, pantographWear));
        }
        if (breakerWear > 75) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Circuit Breaker", 0, breakerWear));
        }
    }

    /**
     * Monitors engine diagnostics and generates alerts for abnormal values.
     *
     * @param vehicleId vehicle identifier
     * @param engineTemp engine temperature in Celsius
     * @param oilPressure oil pressure in PSI
     * @param fuelEfficiency fuel efficiency in km/L
     */
    public void monitorEngineDiagnostics(String vehicleId, double engineTemp, double oilPressure, double fuelEfficiency) {
        if (engineTemp > 90) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Engine Temperature", 0, engineTemp));
        }
        if (oilPressure < 20) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Oil Pressure", 0, oilPressure));
        }
        if (fuelEfficiency < 70) {
            updateComponentStatus(new ComponentStatus(vehicleId, "Fuel Efficiency", 0, fuelEfficiency));
        }
    }

    /**
     * Performs a real-time check of mechanical components and directly updates observers.
     *
     * @param vehicleId vehicle identifier
     * @param brakeWear wear level of brakes
     * @param wheelWear wear level of wheels
     * @param bearingWear wear level of axle bearings
     */
    public void checkMechanicalComponents(int vehicleId, double brakeWear, double wheelWear, double bearingWear) {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.add(new ComponentStatus("Brakes", brakeWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Wheels/Tires", wheelWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Axle Bearings", bearingWear, "Hours", vehicleId));

        for (ComponentObserver observer : observers) {
            observer.update(statuses);
        }
    }

    /**
     * Performs a real-time check of electrical components and notifies observers.
     *
     * @param vehicleId vehicle identifier
     * @param catenaryWear wear level of catenary
     * @param pantographWear wear level of pantograph
     * @param breakerWear wear level of circuit breaker
     */
    public void checkElectricalComponents(int vehicleId, double catenaryWear, double pantographWear, double breakerWear) {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.add(new ComponentStatus("Catenary", catenaryWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Pantograph", pantographWear, "Hours", vehicleId));
        statuses.add(new ComponentStatus("Circuit Breaker", breakerWear, "Hours", vehicleId));

        for (ComponentObserver observer : observers) {
            observer.update(statuses);
        }
    }

    /**
     * Performs a real-time check of engine diagnostics and notifies observers.
     *
     * @param vehicleId vehicle identifier
     * @param engineTemp engine temperature in Celsius
     * @param oilPressure oil pressure in PSI
     * @param fuelEfficiency fuel efficiency in km/L
     */
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
