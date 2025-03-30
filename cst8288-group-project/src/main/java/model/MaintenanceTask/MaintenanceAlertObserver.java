package model.MaintenanceTask;

import businesslayer.MaintenanceTaskController;
import java.util.List;

public class MaintenanceAlertObserver implements ComponentObserver {
    private final MaintenanceTaskController taskController;
    
    public MaintenanceAlertObserver(MaintenanceTaskController taskController) {
        this.taskController = taskController;
    }
    
    @Override
    public void update(List<ComponentStatus> statuses) {
        for (ComponentStatus status : statuses) {
            if (status.getStatus().equals("CRITICAL")) {
                // Create emergency maintenance task
                try {
                    taskController.createMaintenanceTask(
                        status.getVehicleId(),
                        status.getComponentType(),
                        status.getAlertMessage(),
                        java.time.LocalDateTime.now(),
                        "System"
                    );
                } catch (Exception e) {
                    System.err.println("Failed to create maintenance task: " + e.getMessage());
                }
            }
        }
    }
} 