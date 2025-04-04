package model.MaintenanceTask;

import java.time.LocalDateTime;

public class MaintenanceAlert {
    private ComponentStatus componentStatus;
    private LocalDateTime timestamp;
    
    public MaintenanceAlert(ComponentStatus componentStatus) {
        this.componentStatus = componentStatus;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public ComponentStatus getComponentStatus() { return componentStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Setters
    public void setComponentStatus(ComponentStatus componentStatus) { this.componentStatus = componentStatus; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
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