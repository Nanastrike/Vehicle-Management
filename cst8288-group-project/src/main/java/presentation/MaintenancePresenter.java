package presentation;

import businesslayer.MaintenanceTaskController;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MaintenancePresenter {
    private final MaintenanceTaskController taskController;
    private final VehicleComponentMonitor componentMonitor;
    
    public MaintenancePresenter() throws SQLException {
        this.taskController = new MaintenanceTaskController();
        this.componentMonitor = new VehicleComponentMonitor();
    }
    
    public void displayMaintenanceTasks() throws SQLException {
        List<MaintenanceTask> tasks = taskController.getAllMaintenanceTasks();
        for (MaintenanceTask task : tasks) {
            System.out.println("Task ID: " + task.getTaskId());
            System.out.println("Vehicle ID: " + task.getVehicleId());
            System.out.println("Component Type: " + task.getComponentType());
            System.out.println("Description: " + task.getTaskDescription());
            System.out.println("Scheduled Date: " + task.getScheduledDate());
            System.out.println("Status: " + task.getStatus());
            System.out.println("Created By: " + task.getCreatedBy());
            System.out.println("Created At: " + task.getCreatedAt());
            System.out.println("----------------------------------------");
        }
    }
    
    public void displayComponentAlerts() {
        List<ComponentStatus> statuses = componentMonitor.getComponentStatuses();
        for (ComponentStatus status : statuses) {
            MaintenanceAlert alert = new MaintenanceAlert(status);
            System.out.println(alert.toString());
        }
    }
} 