package model.MaintenanceTask;

import model.MaintenanceTask.MaintenanceTaskManager;
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
                        status.getComponentType(),
                        "EMERGENCY: " + status.getAlertMessage(),
                        LocalDateTime.now(),
                        "System"
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