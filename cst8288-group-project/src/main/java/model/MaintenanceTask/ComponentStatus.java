package model.MaintenanceTask;

public class ComponentStatus {
    private String vehicleId;
    private String componentType;
    private double wearLevel;
    private String status;
    private String alertMessage;
    
    public ComponentStatus(String vehicleId, String componentType, double wearLevel, String status, String alertMessage) {
        this.vehicleId = vehicleId;
        this.componentType = componentType;
        this.wearLevel = wearLevel;
        this.status = status;
        this.alertMessage = alertMessage;
    }
    
    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getComponentType() { return componentType; }
    public double getWearLevel() { return wearLevel; }
    public String getStatus() { return status; }
    public String getAlertMessage() { return alertMessage; }
    
    // Setters
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public void setWearLevel(double wearLevel) { this.wearLevel = wearLevel; }
    public void setStatus(String status) { this.status = status; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
} 