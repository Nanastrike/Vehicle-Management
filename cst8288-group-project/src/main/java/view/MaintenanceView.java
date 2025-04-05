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

/**
 * MaintenanceView is responsible for interacting with users and displaying maintenance-related data.
 * It acts as a view component in the MVC pattern and implements the ComponentObserver interface to 
 * receive component status updates and generate alerts.
 */
public class MaintenanceView implements ComponentObserver {
    private final Scanner scanner;
    private final MaintenanceTaskManager taskManager;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    /**
     * Constructs a MaintenanceView with the given HTTP request and response.
     * 
     * @param request the HTTP request object
     * @param response the HTTP response object
     * @throws SQLException if database connection fails
     */
    public MaintenanceView(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        this.scanner = new Scanner(System.in);
        this.taskManager = new MaintenanceTaskManager();
        this.request = request;
        this.response = response;
    }

    /**
     * Starts the CLI-based interaction for viewing maintenance tasks.
     * Offers a simple menu system for displaying tasks or alerts.
     */
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
                        // Feature not yet implemented
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

    /**
     * Displays the main menu options to the user.
     */
    private void displayMainMenu() {
        System.out.println("\nVehicle Maintenance Management System");
        System.out.println("1. Display All Maintenance Tasks");
        System.out.println("2. Display Component Alerts");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Updates the view with new component statuses and prints related alerts to the console.
     * 
     * @param statuses a list of component statuses that have been updated
     */
    @Override
    public void update(List<ComponentStatus> statuses) {
        for (ComponentStatus status : statuses) {
            MaintenanceAlert alert = new MaintenanceAlert(status);
            System.out.println("\nNew Alert:");
            System.out.println(alert.toString());
        }
    }

    /**
     * Sets the list of maintenance tasks to be displayed via the servlet request.
     * 
     * @param tasks list of maintenance tasks
     */
    public void displayMaintenanceTasks(List<MaintenanceTask> tasks) {
        request.setAttribute("maintenanceTasks", tasks);
    }

    /**
     * Sets the list of maintenance alerts to be displayed via the servlet request.
     * 
     * @param alerts list of maintenance alerts
     */
    public void displayComponentAlerts(List<MaintenanceAlert> alerts) {
        request.setAttribute("maintenanceAlerts", alerts);
    }

    /**
     * Sets mechanical component statuses in the request attribute for UI display.
     * 
     * @param statuses list of mechanical component statuses
     */
    public void setMechanicalStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("mechanicalStatuses", statuses);
    }

    /**
     * Sets electrical component statuses in the request attribute for UI display.
     * 
     * @param statuses list of electrical component statuses
     */
    public void setElectricalStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("electricalStatuses", statuses);
    }

    /**
     * Sets engine component statuses in the request attribute for UI display.
     * 
     * @param statuses list of engine component statuses
     */
    public void setEngineStatuses(List<ComponentStatus> statuses) {
        request.setAttribute("engineStatuses", statuses);
    }
}
