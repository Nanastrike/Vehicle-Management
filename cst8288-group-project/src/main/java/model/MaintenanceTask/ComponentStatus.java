package model.MaintenanceTask;

import java.time.LocalDateTime;

/**
 * Represents the status and condition of a vehicle component in the maintenance system.
 * This class tracks various metrics including wear level, usage hours, and operational status
 * of vehicle components, and generates appropriate alert messages based on these metrics.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 */
public class ComponentStatus {
    /** Unique identifier for the component */
    private int componentId;
    /** Identifier of the vehicle containing this component */
    private String vehicleId;
    /** Name or description of the component */
    private String componentName;
    /** Total hours the component has been in use */
    private int hoursUsed;
    /** Current wear level of the component (percentage) */
    private double wearLevel;
    /** Timestamp of the last status update */
    private LocalDateTime lastUpdated;
    /** Current operational status (NORMAL, WARNING, or CRITICAL) */
    private String status;
    /** Alert message based on component status */
    private String alertMessage;
    /** Unit of measurement for component values */
    private String unit;
    /** Current measured value of the component */
    private double value;
    
    /**
     * Constructs a new ComponentStatus with wear-based metrics.
     *
     * @param vehicleId The ID of the vehicle containing this component
     * @param componentName The name of the component
     * @param hoursUsed Total hours the component has been in use
     * @param wearLevel Current wear level of the component (percentage)
     */
    public ComponentStatus(String vehicleId, String componentName, int hoursUsed, double wearLevel) {
        this.vehicleId = vehicleId;
        this.componentName = componentName;
        this.hoursUsed = hoursUsed;
        this.wearLevel = wearLevel;
        this.lastUpdated = LocalDateTime.now();
        this.status = wearLevel > 80 ? "CRITICAL" : (wearLevel > 60 ? "WARNING" : "NORMAL");
        this.alertMessage = generateAlertMessage();
    }
    
    /**
     * Constructs a new ComponentStatus with value-based metrics.
     *
     * @param componentName The name of the component
     * @param value The measured value of the component
     * @param unit The unit of measurement
     * @param vehicleId The ID of the vehicle containing this component
     */
    public ComponentStatus(String componentName, double value, String unit, int vehicleId) {
        this.componentName = componentName;
        this.value = value;
        this.unit = unit;
        this.vehicleId = String.valueOf(vehicleId);
        this.status = calculateStatus(value);
        this.alertMessage = generateAlertMessage();
    }
    
    /**
     * Generates an appropriate alert message based on the component's status.
     * @return A descriptive alert message
     */
    private String generateAlertMessage() {
        switch (status) {
            case "CRITICAL":
                return componentName + " requires immediate attention!";
            case "WARNING":
                return componentName + " needs maintenance soon";
            default:
                return componentName + " status normal";
        }
    }
    
    /**
     * Calculates the status level based on a component's value.
     * @param value The measured value to evaluate
     * @return The status level (CRITICAL, WARNING, or NORMAL)
     */
    private String calculateStatus(double value) {
        if (value > 80) {
            return "CRITICAL";
        } else if (value > 60) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }
    
    // Getters
    /** @return The unique identifier of the component */
    public int getComponentId() { return componentId; }
    /** @return The identifier of the vehicle containing this component */
    public String getVehicleId() { return vehicleId; }
    /** @return The name of the component */
    public String getComponentName() { return componentName; }
    /** @return Total hours the component has been in use */
    public int getHoursUsed() { return hoursUsed; }
    /** @return Current wear level of the component */
    public double getWearLevel() { return wearLevel; }
    /** @return Timestamp of the last status update */
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    /** @return Current operational status */
    public String getStatus() { return status; }
    /** @return Current alert message */
    public String getAlertMessage() { return alertMessage; }
    /** @return Unit of measurement */
    public String getUnit() { return unit; }
    /** @return Current measured value */
    public double getValue() { return value; }
    
    // Setters
    /** @param componentId The component identifier to set */
    public void setComponentId(int componentId) { this.componentId = componentId; }
    /** @param vehicleId The vehicle identifier to set */
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    /** @param componentName The component name to set */
    public void setComponentName(String componentName) { this.componentName = componentName; }
    /** @param hoursUsed The hours used to set */
    public void setHoursUsed(int hoursUsed) { this.hoursUsed = hoursUsed; }
    /** @param wearLevel The wear level to set */
    public void setWearLevel(double wearLevel) { this.wearLevel = wearLevel; }
    /** @param lastUpdated The last updated timestamp to set */
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    /** @param status The status to set */
    public void setStatus(String status) { this.status = status; }
    /** @param alertMessage The alert message to set */
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    /** @param unit The unit of measurement to set */
    public void setUnit(String unit) { this.unit = unit; }
    /** @param value The measured value to set */
    public void setValue(double value) { this.value = value; }
} 