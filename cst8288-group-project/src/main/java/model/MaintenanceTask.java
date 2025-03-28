package model;

import java.time.LocalDateTime;

public class MaintenanceTask {
    private int taskId;
    private String vehicleId;
    private String componentType;
    private String taskDescription;
    private LocalDateTime scheduledDate;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    
    public MaintenanceTask(String vehicleId, String componentType, 
                          String taskDescription, LocalDateTime scheduledDate, 
                          String createdBy) {
        this.vehicleId = vehicleId;
        this.componentType = componentType;
        this.taskDescription = taskDescription;
        this.scheduledDate = scheduledDate;
        this.status = "PENDING";
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public int getTaskId() { return taskId; }
    public String getVehicleId() { return vehicleId; }
    public String getComponentType() { return componentType; }
    public String getTaskDescription() { return taskDescription; }
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setters
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 