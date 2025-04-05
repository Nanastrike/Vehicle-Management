package model.MaintenanceTask;

import java.util.List;
import java.time.LocalDateTime;

/**
 * Observer implementation that creates maintenance tasks for components in critical condition.
 * This class monitors component statuses and automatically generates maintenance tasks
 * when components reach critical status levels.
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since Java 1.21
 * @see ComponentObserver
 * @see MaintenanceTaskManager
 */
public class MaintenanceAlertObserver implements ComponentObserver {
    /** Task manager for creating maintenance tasks */
    private final MaintenanceTaskManager taskManager;
    
    /**
     * Constructs a new maintenance alert observer.
     * @param taskManager The task manager to use for creating maintenance tasks
     */
    public MaintenanceAlertObserver(MaintenanceTaskManager taskManager) {
        this.taskManager = taskManager;
    }
    
    /**
     * Updates the observer with new component statuses and creates maintenance tasks
     * for components in critical condition.
     *
     * @param statuses List of component status objects to evaluate
     */
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
                        "HIGH"  // Set emergency tasks to high priority
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