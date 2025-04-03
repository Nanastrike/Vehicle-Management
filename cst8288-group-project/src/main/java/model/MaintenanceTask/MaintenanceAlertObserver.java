package model.MaintenanceTask;

import java.util.List;
import java.time.LocalDateTime;

public class MaintenanceAlertObserver implements ComponentObserver {
    private final MaintenanceTaskManager taskManager;
    
    public MaintenanceAlertObserver(MaintenanceTaskManager taskManager) {
        this.taskManager = taskManager;
    }
    
    @Override
    public void update(List<ComponentStatus> statuses) {
        for (ComponentStatus status : statuses) {
            if (status.getStatus().equals("CRITICAL")) {
                // Create emergency maintenance task
                try {
                    // Convert vehicleId from String to int
                    int vehicleId = Integer.parseInt(status.getVehicleId());
                    
                    taskManager.createMaintenanceTask(
                        vehicleId,
                        "EMERGENCY: " + status.getComponentName(),
                        LocalDateTime.now(),
                        "System",
                        "HIGH"  // 緊急任務設置為高優先級
                    );
                } catch (NumberFormatException e) {
                    System.err.println("Invalid vehicle ID format: " + status.getVehicleId());
                } catch (Exception e) {
                    System.err.println("Failed to create maintenance task: " + e.getMessage());
                }
            }
        }
    }
} 