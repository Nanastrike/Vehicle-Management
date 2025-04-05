package model.MaintenanceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameter container class for component monitoring operations.
 * This class encapsulates all necessary parameters for monitoring different types
 * of vehicle components, including mechanical, electrical, and engine components.
 * It provides a flexible way to store and retrieve monitoring parameters for
 * different component types.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentType
 * @see VehicleComponentMonitor
 */
public class ComponentMonitoringParams {
    /** The ID of the vehicle being monitored */
    private final String vehicleId;
    /** The type of component being monitored */
    private final ComponentType componentType;
    /** Map storing parameter names and their corresponding values */
    private final Map<String, Double> parameters;
    
    /**
     * Constructs a new parameter container for component monitoring.
     *
     * @param vehicleId The ID of the vehicle to monitor
     * @param componentType The type of component to monitor (MECHANICAL, ELECTRICAL, or ENGINE)
     */
    public ComponentMonitoringParams(String vehicleId, ComponentType componentType) {
        this.vehicleId = vehicleId;
        this.componentType = componentType;
        this.parameters = new HashMap<>();
    }
    
    /**
     * Adds a monitoring parameter with its value.
     * Parameters can include metrics like wear levels, temperatures, pressures, etc.
     *
     * @param name The name of the parameter (e.g., "brakeWear", "engineTemp")
     * @param value The value of the parameter
     */
    public void addParameter(String name, double value) {
        parameters.put(name, value);
    }
    
    /**
     * Gets the ID of the vehicle being monitored.
     * @return The vehicle ID
     */
    public String getVehicleId() {
        return vehicleId;
    }
    
    /**
     * Gets the type of component being monitored.
     * @return The component type (MECHANICAL, ELECTRICAL, or ENGINE)
     */
    public ComponentType getComponentType() {
        return componentType;
    }
    
    /**
     * Gets all monitoring parameters and their values.
     * Returns a new HashMap to prevent modification of internal state.
     *
     * @return A map containing all parameter names and their values
     */
    public Map<String, Double> getParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Gets the value of a specific parameter.
     *
     * @param name The name of the parameter to retrieve
     * @return The value of the parameter, or null if not found
     */
    public Double getParameter(String name) {
        return parameters.get(name);
    }
} 