package view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.ComponentObserver;
import model.MaintenanceTask.MaintenanceAlert;
import model.MaintenanceTask.MaintenanceTaskManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MaintenanceView implements ComponentObserver {
    private final Scanner scanner;
    private final MaintenanceTaskManager taskManager;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    public MaintenanceView(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        this.scanner = new Scanner(System.in);
        this.taskManager = new MaintenanceTaskManager();
        this.request = request;
        this.response = response;
    }
    
    public void start() {
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1":
                        displayMaintenanceTasks(taskManager.getAllMaintenanceTasks());
                        break;
                    case "2":
                        // TODO: Implement component alerts display
                        System.out.println("Component alerts feature not implemented yet");
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
        request.setAttribute("maintenanceTasks", tasks);
    }
    
    public void displayComponentAlerts(List<MaintenanceAlert> alerts) {
        request.setAttribute("maintenanceAlerts", alerts);
    }
    
    public void setMechanicalStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("mechanicalStatuses", statuses);
    }
    
    public void setElectricalStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("electricalStatuses", statuses);
    }
    
    public void setEngineStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("engineStatuses", statuses);
    }
} 