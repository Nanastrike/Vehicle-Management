package model;

public class MaintenanceAlert {
    private String vehicleId;
    private String componentType;
    private double wearLevel;
    private String alertMessage;
    private String timestamp;
    
    public MaintenanceAlert(String vehicleId, String componentType, double wearLevel, String alertMessage) {
        this.vehicleId = vehicleId;
        this.componentType = componentType;
        this.wearLevel = wearLevel;
        this.alertMessage = alertMessage;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getComponentType() { return componentType; }
    public double getWearLevel() { return wearLevel; }
    public String getAlertMessage() { return alertMessage; }
    public String getTimestamp() { return timestamp; }
    
    // Setters
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public void setWearLevel(double wearLevel) { this.wearLevel = wearLevel; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
} 