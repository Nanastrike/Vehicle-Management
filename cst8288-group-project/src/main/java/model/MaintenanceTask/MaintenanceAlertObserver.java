package model.MaintenanceTask;

import model.MaintenanceTask.MaintenanceTaskManager;
import java.util.List;

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
                    taskManager.createMaintenanceTask(
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