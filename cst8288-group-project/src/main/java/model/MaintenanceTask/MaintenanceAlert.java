package model.MaintenanceTask;

import java.time.LocalDateTime;

/**
 * Represents a maintenance alert generated for a component that requires attention.
 * This class combines component status information with a timestamp to track when
 * the alert was generated.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentStatus
 */
public class MaintenanceAlert {
    /** The component status that triggered this alert */
    private ComponentStatus componentStatus;
    /** The time when this alert was generated */
    private LocalDateTime timestamp;
    
    /**
     * Constructs a new maintenance alert for a component.
     * Sets the timestamp to the current time.
     *
     * @param componentStatus The status of the component that triggered the alert
     */
    public MaintenanceAlert(ComponentStatus componentStatus) {
        this.componentStatus = componentStatus;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    /** @return The component status associated with this alert */
    public ComponentStatus getComponentStatus() { return componentStatus; }
    /** @return The time when this alert was generated */
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Setters
    /** @param componentStatus The component status to set */
    public void setComponentStatus(ComponentStatus componentStatus) { this.componentStatus = componentStatus; }
    /** @param timestamp The timestamp to set */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    /**
     * Provides a string representation of the maintenance alert.
     * Includes the timestamp, vehicle ID, component name, and wear level.
     *
     * @return A formatted string containing alert details
     */
    @Override
    public String toString() {
        return String.format("Alert at %s: %s - %s (Wear: %.1f%%)",
            timestamp,
            componentStatus.getVehicleId(),
            componentStatus.getComponentName(),
            componentStatus.getWearLevel()
        );
    }
} 