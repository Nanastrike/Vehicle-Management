package model.MaintenanceTask;

import java.time.LocalDateTime;

public class ComponentStatus {
    private int componentId;
    private String vehicleId;
    private String componentName;
    private int hoursUsed;
    private double wearLevel;
    private LocalDateTime lastUpdated;
    private String status;
    private String alertMessage;
    private String unit;
    private double value;
    
    public ComponentStatus(String vehicleId, String componentName, int hoursUsed, double wearLevel) {
        this.vehicleId = vehicleId;
        this.componentName = componentName;
        this.hoursUsed = hoursUsed;
        this.wearLevel = wearLevel;
        this.lastUpdated = LocalDateTime.now();
        this.status = wearLevel > 80 ? "CRITICAL" : (wearLevel > 60 ? "WARNING" : "NORMAL");
        this.alertMessage = generateAlertMessage();
    }
    
    public ComponentStatus(String componentName, double value, String unit, int vehicleId) {
        this.componentName = componentName;
        this.value = value;
        this.unit = unit;
        this.vehicleId = String.valueOf(vehicleId);
        this.status = calculateStatus(value);
        this.alertMessage = generateAlertMessage();
    }
    
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
    public int getComponentId() { return componentId; }
    public String getVehicleId() { return vehicleId; }
    public String getComponentName() { return componentName; }
    public int getHoursUsed() { return hoursUsed; }
    public double getWearLevel() { return wearLevel; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public String getStatus() { return status; }
    public String getAlertMessage() { return alertMessage; }
    public String getUnit() { return unit; }
    public double getValue() { return value; }
    
    // Setters
    public void setComponentId(int componentId) { this.componentId = componentId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setComponentName(String componentName) { this.componentName = componentName; }
    public void setHoursUsed(int hoursUsed) { this.hoursUsed = hoursUsed; }
    public void setWearLevel(double wearLevel) { this.wearLevel = wearLevel; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setStatus(String status) { this.status = status; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setValue(double value) { this.value = value; }
} 