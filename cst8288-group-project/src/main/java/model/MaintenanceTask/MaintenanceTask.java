package model.MaintenanceTask;

import java.time.LocalDateTime;

public class MaintenanceTask {
    private int taskId;
    private String vehicleId;
    private String taskType;
    private String description;
    private LocalDateTime scheduledDate;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    
    // Default constructor
    public MaintenanceTask() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
    
    // Existing constructor
    public MaintenanceTask(String vehicleId, String taskType, 
                          String description, LocalDateTime scheduledDate, 
                          String createdBy) {
        this.vehicleId = vehicleId;
        this.taskType = taskType;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.status = "PENDING";
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public int getTaskId() { return taskId; }
    public String getVehicleId() { return vehicleId; }
    public String getTaskType() { return taskType; }
    public String getDescription() { return description; }
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setters
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public void setDescription(String description) { this.description = description; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 