package model.MaintenanceTask;

import java.time.LocalDateTime;

/**
 * Represents a maintenance task for a vehicle component in the maintenance management system.
 * This class encapsulates all information related to a specific maintenance task,
 * including scheduling, status tracking, and task prioritization.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 */
public class MaintenanceTask {
    /** Unique identifier for the maintenance task */
    private int taskId;
    /** Identifier of the vehicle requiring maintenance */
    private String vehicleId;
    /** Vehicle's registration or identification number */
    private String vehicleNumber;
    /** Identifier of the component requiring maintenance */
    private int componentId;
    /** Type of maintenance task to be performed */
    private String taskType;
    /** Scheduled date and time for the maintenance */
    private LocalDateTime scheduledDate;
    /** Current status of the maintenance task */
    private String status;
    /** User or system that created the task */
    private String createdBy;
    /** Date and time when the task was created */
    private LocalDateTime createdAt;
    /** Priority level of the maintenance task */
    private String priority;
    
    /**
     * Default constructor for MaintenanceTask.
     * Initializes a new task with default status "Scheduled",
     * current timestamp, and "MEDIUM" priority.
     */
    public MaintenanceTask() {
        this.status = "Scheduled";
        this.createdAt = LocalDateTime.now();
        this.priority = "MEDIUM"; // Default priority
    }
    
    /**
     * Constructs a new maintenance task with specified parameters.
     *
     * @param vehicleId The ID of the vehicle requiring maintenance
     * @param taskType The type of maintenance task to be performed
     * @param scheduledDate The scheduled date and time for the maintenance
     * @param createdBy The user or system creating the task
     */
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
    /** @return The unique identifier of the maintenance task */
    public int getTaskId() { return taskId; }
    /** @return The identifier of the vehicle requiring maintenance */
    public String getVehicleId() { return vehicleId; }
    /** @return The vehicle's registration or identification number */
    public String getVehicleNumber() { return vehicleNumber; }
    /** @return The identifier of the component requiring maintenance */
    public int getComponentId() { return componentId; }
    /** @return The type of maintenance task */
    public String getTaskType() { return taskType; }
    /** @return The scheduled date and time for the maintenance */
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    /** @return The current status of the maintenance task */
    public String getStatus() { return status; }
    /** @return The user or system that created the task */
    public String getCreatedBy() { return createdBy; }
    /** @return The date and time when the task was created */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @return The priority level of the maintenance task */
    public String getPriority() { return priority; }
    
    // Setters
    /** @param taskId The unique identifier to set for the maintenance task */
    public void setTaskId(int taskId) { this.taskId = taskId; }
    /** @param vehicleId The vehicle identifier to set */
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    /** @param vehicleNumber The vehicle registration number to set */
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    /** @param componentId The component identifier to set */
    public void setComponentId(int componentId) { this.componentId = componentId; }
    /** @param taskType The type of maintenance task to set */
    public void setTaskType(String taskType) { this.taskType = taskType; }
    /** @param scheduledDate The scheduled date and time to set */
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    /** @param status The status to set for the maintenance task */
    public void setStatus(String status) { this.status = status; }
    /** @param createdBy The creator of the task to set */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    /** @param createdAt The creation date and time to set */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** @param priority The priority level to set for the task */
    public void setPriority(String priority) { this.priority = priority; }
} 