package view;

import businesslayer.MaintenanceTaskController;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlertObserver;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MaintenanceView {
    private final MaintenanceTaskController taskController;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;
    private final VehicleComponentMonitor componentMonitor;
    private final MaintenanceAlertObserver alertObserver;

    public MaintenanceView() throws SQLException {
        this.taskController = new MaintenanceTaskController();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.componentMonitor = new VehicleComponentMonitor();
        this.alertObserver = new MaintenanceAlertObserver(taskController);
        this.componentMonitor.addObserver(alertObserver);
    }

    public void displayMaintenanceMenu() {
        while (true) {
            System.out.println("\n=== Vehicle Maintenance Management System ===");
            System.out.println("1. Display All Maintenance Tasks");
            System.out.println("2. Add New Maintenance Task");
            System.out.println("3. Update Task Status");
            System.out.println("4. Delete Maintenance Task");
            System.out.println("5. Monitor Mechanical Components");
            System.out.println("6. Monitor Electrical Components");
            System.out.println("7. Monitor Engine Diagnostics");
            System.out.println("8. Display Component Alerts");
            System.out.println("0. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            try {
                switch (choice) {
                    case 1:
                        displayAllTasks();
                        break;
                    case 2:
                        addNewTask();
                        break;
                    case 3:
                        updateTaskStatus();
                        break;
                    case 4:
                        deleteTask();
                        break;
                    case 5:
                        monitorMechanicalComponents();
                        break;
                    case 6:
                        monitorElectricalComponents();
                        break;
                    case 7:
                        monitorEngineDiagnostics();
                        break;
                    case 8:
                        displayComponentAlerts();
                        break;
                    case 0:
                        System.out.println("Thank you for using the system!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void displayAllTasks() throws SQLException {
        List<MaintenanceTask> tasks = taskController.getAllMaintenanceTasks();
        if (tasks.isEmpty()) {
            System.out.println("No maintenance tasks found.");
            return;
        }

        System.out.println("\n=== All Maintenance Tasks ===");
        for (MaintenanceTask task : tasks) {
            System.out.println("Task ID: " + task.getTaskId());
            System.out.println("Vehicle ID: " + task.getVehicleId());
            System.out.println("Component Type: " + task.getComponentType());
            System.out.println("Description: " + task.getTaskDescription());
            System.out.println("Scheduled Date: " + task.getScheduledDate().format(dateFormatter));
            System.out.println("Status: " + task.getStatus());
            System.out.println("Created By: " + task.getCreatedBy());
            System.out.println("Created At: " + task.getCreatedAt().format(dateFormatter));
            System.out.println("----------------------------------------");
        }
    }

    private void addNewTask() throws SQLException {
        System.out.println("\n=== Add New Maintenance Task ===");
        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();
        System.out.print("Component Type: ");
        String componentType = scanner.nextLine();
        System.out.print("Task Description: ");
        String description = scanner.nextLine();
        System.out.print("Scheduled Date (yyyy-MM-dd HH:mm): ");
        String dateStr = scanner.nextLine();
        System.out.print("Created By: ");
        String createdBy = scanner.nextLine();

        LocalDateTime scheduledDate = LocalDateTime.parse(dateStr, dateFormatter);
        taskController.createMaintenanceTask(vehicleId, componentType, description, scheduledDate, createdBy);
        System.out.println("Maintenance task created successfully!");
    }

    private void updateTaskStatus() throws SQLException {
        System.out.println("\n=== Update Task Status ===");
        System.out.print("Task ID: ");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Clear newline
        System.out.print("New Status (PENDING/IN_PROGRESS/COMPLETED): ");
        String status = scanner.nextLine();

        taskController.updateTaskStatus(taskId, status);
        System.out.println("Task status updated successfully!");
    }

    private void deleteTask() throws SQLException {
        System.out.println("\n=== Delete Maintenance Task ===");
        System.out.print("Task ID: ");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        taskController.deleteMaintenanceTask(taskId);
        System.out.println("Task deleted successfully!");
    }

    private void monitorMechanicalComponents() {
        System.out.println("\n=== Monitor Mechanical Components ===");
        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();
        System.out.print("Brake Wear Level (0-100): ");
        double brakeWear = scanner.nextDouble();
        System.out.print("Wheel Wear Level (0-100): ");
        double wheelWear = scanner.nextDouble();
        System.out.print("Bearing Wear Level (0-100): ");
        double bearingWear = scanner.nextDouble();
        scanner.nextLine(); // Clear newline

        componentMonitor.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
        System.out.println("Mechanical components monitoring completed!");
    }

    private void monitorElectricalComponents() {
        System.out.println("\n=== Monitor Electrical Components ===");
        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();
        System.out.print("Catenary Wear Level (0-100): ");
        double catenaryWear = scanner.nextDouble();
        System.out.print("Pantograph Wear Level (0-100): ");
        double pantographWear = scanner.nextDouble();
        System.out.print("Circuit Breaker Wear Level (0-100): ");
        double breakerWear = scanner.nextDouble();
        scanner.nextLine(); // Clear newline

        componentMonitor.monitorElectricalComponents(vehicleId, catenaryWear, pantographWear, breakerWear);
        System.out.println("Electrical components monitoring completed!");
    }

    private void monitorEngineDiagnostics() {
        System.out.println("\n=== Monitor Engine Diagnostics ===");
        System.out.print("Vehicle ID: ");
        String vehicleId = scanner.nextLine();
        System.out.print("Engine Temperature (0-100): ");
        double engineTemp = scanner.nextDouble();
        System.out.print("Oil Pressure (0-100): ");
        double oilPressure = scanner.nextDouble();
        System.out.print("Fuel Efficiency (0-100): ");
        double fuelEfficiency = scanner.nextDouble();
        scanner.nextLine(); // Clear newline

        componentMonitor.monitorEngineDiagnostics(vehicleId, engineTemp, oilPressure, fuelEfficiency);
        System.out.println("Engine diagnostics monitoring completed!");
    }

    private void displayComponentAlerts() {
        System.out.println("\n=== Component Alerts ===");
        List<ComponentStatus> statuses = componentMonitor.getComponentStatuses();
        if (statuses.isEmpty()) {
            System.out.println("No component alerts at this time.");
            return;
        }

        for (ComponentStatus status : statuses) {
            System.out.println("Vehicle ID: " + status.getVehicleId());
            System.out.println("Component Type: " + status.getComponentType());
            System.out.println("Wear Level: " + status.getWearLevel() + "%");
            System.out.println("Status: " + status.getStatus());
            System.out.println("Alert Message: " + status.getAlertMessage());
            System.out.println("----------------------------------------");
        }
    }
} 