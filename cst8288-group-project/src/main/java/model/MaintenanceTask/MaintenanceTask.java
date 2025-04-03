package model.MaintenanceTask;

import java.time.LocalDateTime;

public class MaintenanceTask {
    private int taskId;
    private String vehicleId;
    private String vehicleNumber;
    private int componentId;
    private String taskType;
    private LocalDateTime scheduledDate;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    private String priority;
    
    // Default constructor
    public MaintenanceTask() {
        this.status = "Scheduled";
        this.createdAt = LocalDateTime.now();
        this.priority = "MEDIUM"; // Default priority
    }
    
    // Existing constructor
    public MaintenanceTask(String vehicleId, String taskType, 
                          LocalDateTime scheduledDate, String createdBy) {
        this.vehicleId = vehicleId;
        this.taskType = taskType;
        this.scheduledDate = scheduledDate;
        this.status = "Scheduled";
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.priority = "MEDIUM"; // Default priority
    }
    
    // Getters
    public int getTaskId() { return taskId; }
    public String getVehicleId() { return vehicleId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public int getComponentId() { return componentId; }
    public String getTaskType() { return taskType; }
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPriority() { return priority; }
    
    // Setters
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public void setComponentId(int componentId) { this.componentId = componentId; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setPriority(String priority) { this.priority = priority; }
} 