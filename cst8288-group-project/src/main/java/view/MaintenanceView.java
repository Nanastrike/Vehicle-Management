package view;

import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.ComponentObserver;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenancePresenter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MaintenanceView implements ComponentObserver {
    private final Scanner scanner;
    private final MaintenancePresenter presenter;
    
    public MaintenanceView() throws SQLException {
        this.scanner = new Scanner(System.in);
        this.presenter = new MaintenancePresenter(this);
    }
    
    public void start() {
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1":
                        presenter.displayMaintenanceTasks();
                        break;
                    case "2":
                        presenter.displayComponentAlerts();
                        break;
                    case "3":
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\nVehicle Maintenance Management System");
        System.out.println("1. Display All Maintenance Tasks");
        System.out.println("2. Display Component Alerts");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }
    
    @Override
    public void update(List<ComponentStatus> statuses) {
        for (ComponentStatus status : statuses) {
            MaintenanceAlert alert = new MaintenanceAlert(status);
            System.out.println("\nNew Alert:");
            System.out.println(alert.toString());
        }
    }
    
    public void displayMaintenanceTasks(List<MaintenanceTask> tasks) {
        System.out.println("\nMaintenance Tasks:");
        for (MaintenanceTask task : tasks) {
            System.out.println("Task ID: " + task.getTaskId());
            System.out.println("Vehicle ID: " + task.getVehicleId());
            System.out.println("Task Type: " + task.getTaskType());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Scheduled Date: " + task.getScheduledDate());
            System.out.println("Status: " + task.getStatus());
            System.out.println("----------------------------------------");
        }
    }
    
    public void displayComponentAlerts(List<MaintenanceAlert> alerts) {
        System.out.println("\nComponent Alerts:");
        for (MaintenanceAlert alert : alerts) {
            System.out.println(alert.toString());
        }
    }
} 