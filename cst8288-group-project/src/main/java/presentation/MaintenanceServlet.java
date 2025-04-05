package presentation;

import model.MaintenanceTask.MaintenanceTaskManager;
import data.VehicleDAO;
import data.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.VehicleManagement.Vehicle;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import model.MaintenanceTask.ComponentStatus;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for managing vehicle maintenance tasks and component statuses.
 * Handles displaying maintenance data, scheduling new tasks, and updating component conditions.
 */
@WebServlet(name = "MaintenanceServlet", urlPatterns = {"/MaintenanceServlet"})
public class MaintenanceServlet extends HttpServlet {
    private MaintenanceTaskManager taskManager;
    private VehicleComponentMonitor componentMonitor;
    private VehicleDAO vehicleDAO;

    /**
     * Initializes servlet and required managers/DAOs.
     */
    @Override
    public void init() throws ServletException {
        try {
            this.taskManager = new MaintenanceTaskManager();
            this.componentMonitor = new VehicleComponentMonitor();
            this.vehicleDAO = new VehicleDAO(DatabaseConnection.getInstance().getConnection());
            System.out.println("MaintenanceServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing MaintenanceServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize MaintenanceServlet", e);
        }
    }

    /**
     * Handles GET requests for viewing maintenance information and component statuses.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("MaintenanceServlet doGet called");
            String action = request.getParameter("action");

            // Fetch all vehicles from database
            List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
            System.out.println("Retrieved vehicle list size: " + (vehicleList != null ? vehicleList.size() : "null"));

            String selectedVehicle = request.getParameter("vehicleNumber");
            if ("checkComponents".equals(action) && selectedVehicle != null && !selectedVehicle.isEmpty()) {
                Vehicle currentVehicle = null;
                for (Vehicle v : vehicleList) {
                    if (v.getVehicleNumber().equals(selectedVehicle)) {
                        currentVehicle = v;
                        break;
                    }
                }
                request.setAttribute("currentVehicle", currentVehicle);
            }

            // Retrieve previously stored component statuses from session
            HttpSession session = request.getSession();
            List<ComponentStatus> componentStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");
            if (componentStatuses != null) {
                request.setAttribute("componentStatuses", componentStatuses);
            }

            // Debug print vehicle info
            if (vehicleList != null) {
                for (Vehicle v : vehicleList) {
                    System.out.println("Vehicle: ID=" + v.getVehicleID() +
                            ", Number=" + v.getVehicleNumber() +
                            ", Type=" + (v.getVehicleType() != null ? v.getVehicleType().getTypeName() : "null"));
                }
            }

            // Check for vehicles needing maintenance (not serviced in 3 months)
            List<Vehicle> vehiclesNeedingMaintenance = new ArrayList<>();
            LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

            for (Vehicle vehicle : vehicleList) {
                if (vehicle.getLastMaintenanceDate() != null) {
                    LocalDateTime lastMaintenance = vehicle.getLastMaintenanceDate().toLocalDate().atStartOfDay();
                    if (lastMaintenance.isBefore(threeMonthsAgo)) {
                        vehiclesNeedingMaintenance.add(vehicle);
                    }
                }
            }

            request.setAttribute("vehicleList", vehicleList);
            request.setAttribute("vehiclesNeedingMaintenance", vehiclesNeedingMaintenance);

            // Retrieve scheduled maintenance tasks
            List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
            request.setAttribute("scheduledTasks", tasks);

            // Forward to JSP page
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in MaintenanceServlet doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Merges existing and new component statuses into a single list.
     */
    private List<ComponentStatus> mergeComponentStatuses(List<ComponentStatus> existingStatuses, List<ComponentStatus> newStatuses) {
        List<ComponentStatus> mergedStatuses = new ArrayList<>();
        if (existingStatuses != null) {
            mergedStatuses.addAll(existingStatuses);
        }
        if (newStatuses != null) {
            mergedStatuses.addAll(newStatuses);
        }
        return mergedStatuses;
    }

    /**
     * Calculates the number of hours passed since the last maintenance of the given vehicle.
     */
    private int calculateHoursUsed(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLastMaintenanceDate() == null) {
            return 0;
        }

        try {
            java.sql.Date lastMaintenanceDate = vehicle.getLastMaintenanceDate();
            LocalDateTime lastMaintenance = lastMaintenanceDate.toLocalDate().atStartOfDay();
            LocalDateTime now = LocalDateTime.now();
            long hours = java.time.Duration.between(lastMaintenance, now).toHours();
            System.out.println("Hours calculated for vehicle " + vehicle.getVehicleNumber() + ": " + hours);
            return (int) hours;
        } catch (Exception e) {
            System.err.println("Error calculating hours used: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Handles POST requests such as scheduling tasks, updating component statuses, and deletion operations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("scheduleTask".equals(action)) {
                // Handle task scheduling
                String vehicleNumber = request.getParameter("vehicleNumber");
                String taskType = request.getParameter("taskType");
                String scheduledDate = request.getParameter("scheduledDate");
                String priority = request.getParameter("priority");

                System.out.println("Scheduling task for vehicle: " + vehicleNumber);

                taskManager.createMaintenanceTask(
                        Integer.parseInt(vehicleNumber),
                        taskType,
                        LocalDateTime.parse(scheduledDate + "T00:00:00"),
                        "System",
                        priority
                );

                response.sendRedirect("MaintenanceServlet");

            } else if ("deleteTask".equals(action)) {
                // Handle task deletion
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                taskManager.deleteMaintenanceTask(taskId);
                response.sendRedirect("MaintenanceServlet");

            } else if ("deleteComponentStatus".equals(action)) {
                // Handle component status deletion
                String vehicleId = request.getParameter("vehicleId");
                String componentName = request.getParameter("componentName");
                String lastUpdated = request.getParameter("lastUpdated");

                HttpSession session = request.getSession();
                List<ComponentStatus> existingStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");

                if (existingStatuses != null) {
                    if (componentName != null && lastUpdated != null) {
                        // Remove specific component status
                        existingStatuses.removeIf(status ->
                                status.getVehicleId().equals(vehicleId) &&
                                        status.getComponentName().equals(componentName) &&
                                        status.getLastUpdated().toString().equals(lastUpdated));
                    } else {
                        // Remove all statuses for this vehicle
                        existingStatuses.removeIf(status -> status.getVehicleId().equals(vehicleId));
                    }
                    session.setAttribute("componentStatuses", existingStatuses);
                }

                response.sendRedirect("MaintenanceServlet");

            } else {
                // Handle new component status updates
                String vehicleNumber = request.getParameter("vehicleNumber");
                String[] components = request.getParameterValues("components");

                if (vehicleNumber != null && components != null) {
                    HttpSession session = request.getSession();
                    List<ComponentStatus> existingStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");

                    List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
                    Vehicle currentVehicle = vehicles.stream()
                            .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                            .findFirst()
                            .orElse(null);

                    int hoursUsed = calculateHoursUsed(currentVehicle);
                    double wearLevel = 0.0;
                    if (hoursUsed > 0) {
                        // Assume full wear after 2160 hours (3 months)
                        wearLevel = (hoursUsed / 2160.0) * 100.0;
                        wearLevel = Math.min(wearLevel, 100.0);
                    }

                    List<ComponentStatus> newStatuses = new ArrayList<>();
                    for (String component : components) {
                        ComponentStatus status = new ComponentStatus(vehicleNumber, component, hoursUsed, wearLevel);
                        status.setLastUpdated(LocalDateTime.now());
                        newStatuses.add(status);
                    }

                    List<ComponentStatus> mergedStatuses = mergeComponentStatuses(existingStatuses, newStatuses);

                    session.setAttribute("componentStatuses", mergedStatuses);
                    request.setAttribute("componentStatuses", mergedStatuses);
                }

                // Re-populate data for page display
                List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
                List<Vehicle> vehiclesNeedingMaintenance = new ArrayList<>();
                LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

                for (Vehicle vehicle : vehicleList) {
                    if (vehicle.getLastMaintenanceDate() != null) {
                        LocalDateTime lastMaintenance = vehicle.getLastMaintenanceDate().toLocalDate().atStartOfDay();
                        if (lastMaintenance.isBefore(threeMonthsAgo)) {
                            vehiclesNeedingMaintenance.add(vehicle);
                        }
                    }
                }

                List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();

                request.setAttribute("vehicleList", vehicleList);
                request.setAttribute("vehiclesNeedingMaintenance", vehiclesNeedingMaintenance);
                request.setAttribute("scheduledTasks", tasks);

                request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in MaintenanceServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
