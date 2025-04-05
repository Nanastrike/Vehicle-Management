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
import java.util.Date;
import model.MaintenanceTask.ComponentStatus;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "MaintenanceServlet", urlPatterns = {"/MaintenanceServlet"})
public class MaintenanceServlet extends HttpServlet {
    private MaintenanceTaskManager taskManager;
    private VehicleComponentMonitor componentMonitor;
    private VehicleDAO vehicleDAO;
    
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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            System.out.println("MaintenanceServlet doGet called");
            String action = request.getParameter("action");
            
            // Get vehicle list
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
            
            // Get saved component statuses from session
            HttpSession session = request.getSession();
            List<ComponentStatus> componentStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");
            if (componentStatuses != null) {
                request.setAttribute("componentStatuses", componentStatuses);
            }
            
            if (vehicleList != null) {
                for (Vehicle v : vehicleList) {
                    System.out.println("Vehicle: ID=" + v.getVehicleID() + 
                                     ", Number=" + v.getVehicleNumber() + 
                                     ", Type=" + (v.getVehicleType() != null ? v.getVehicleType().getTypeName() : "null"));
                }
            }
            
            // 檢查需要維修的車輛
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
            
            // Get maintenance tasks
            List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
            request.setAttribute("scheduledTasks", tasks);
            
            // Forward to maintenance.jsp
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in MaintenanceServlet doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    private List<ComponentStatus> mergeComponentStatuses(List<ComponentStatus> existingStatuses, List<ComponentStatus> newStatuses) {
        List<ComponentStatus> mergedStatuses = new ArrayList<>();
        
        // Add existing statuses
        if (existingStatuses != null) {
            mergedStatuses.addAll(existingStatuses);
        }
        
        // Add new statuses
        if (newStatuses != null) {
            mergedStatuses.addAll(newStatuses);
        }
        
        return mergedStatuses;
    }
    
    private int calculateHoursUsed(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLastMaintenanceDate() == null) {
            return 0;
        }
        
        try {
            // 獲取最後維修日期
            java.sql.Date lastMaintenanceDate = vehicle.getLastMaintenanceDate();
            LocalDateTime lastMaintenance = lastMaintenanceDate.toLocalDate().atStartOfDay();
            LocalDateTime now = LocalDateTime.now();
            
            // 計算小時差異
            long hours = java.time.Duration.between(lastMaintenance, now).toHours();
            System.out.println("Hours calculated for vehicle " + vehicle.getVehicleNumber() + ": " + hours);
            return (int) hours;
        } catch (Exception e) {
            System.err.println("Error calculating hours used: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("scheduleTask".equals(action)) {
                String vehicleNumber = request.getParameter("vehicleNumber");
                String taskType = request.getParameter("taskType");
                String scheduledDate = request.getParameter("scheduledDate");
                String priority = request.getParameter("priority");
                
                System.out.println("Scheduling task for vehicle: " + vehicleNumber);
                
                // Create maintenance task
                taskManager.createMaintenanceTask(
                    Integer.parseInt(vehicleNumber),
                    taskType,
                    LocalDateTime.parse(scheduledDate + "T00:00:00"),
                    "System",
                    priority
                );
                
                response.sendRedirect("MaintenanceServlet");
            } else if ("deleteTask".equals(action)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                taskManager.deleteMaintenanceTask(taskId);
                response.sendRedirect("MaintenanceServlet");
            } else if ("deleteComponentStatus".equals(action)) {
                // Get parameters
                String vehicleId = request.getParameter("vehicleId");
                String componentName = request.getParameter("componentName");
                String lastUpdated = request.getParameter("lastUpdated");
                
                System.out.println("Attempting to delete component status for vehicle: " + vehicleId);
                System.out.println("Component name: " + componentName);
                System.out.println("Last updated: " + lastUpdated);
                
                // Get existing statuses from session
                HttpSession session = request.getSession();
                List<ComponentStatus> existingStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");
                
                if (existingStatuses != null) {
                    System.out.println("Current statuses size: " + existingStatuses.size());
                    System.out.println("Current statuses: ");
                    for (ComponentStatus status : existingStatuses) {
                        System.out.println("Vehicle: " + status.getVehicleId() + ", Component: " + status.getComponentName());
                    }
                    
                    if (componentName != null && lastUpdated != null) {
                        // Remove specific component status
                        existingStatuses.removeIf(status -> 
                            status.getVehicleId().equals(vehicleId) && 
                            status.getComponentName().equals(componentName) &&
                            status.getLastUpdated().toString().equals(lastUpdated));
                    } else {
                        // Remove all component statuses for the vehicle
                        int sizeBefore = existingStatuses.size();
                        existingStatuses.removeIf(status -> {
                            boolean matches = status.getVehicleId().equals(vehicleId);
                            System.out.println("Checking status - Vehicle ID: " + status.getVehicleId() + 
                                             ", Target ID: " + vehicleId + 
                                             ", Matches: " + matches);
                            return matches;
                        });
                        System.out.println("Removed " + (sizeBefore - existingStatuses.size()) + " statuses");
                    }
                    
                    // Update session
                    session.setAttribute("componentStatuses", existingStatuses);
                    System.out.println("Updated session with " + existingStatuses.size() + " statuses");
                } else {
                    System.out.println("No existing statuses found in session");
                }
                
                // Redirect back to maintenance page
                response.sendRedirect("MaintenanceServlet");
            } else {
                // Handle component status update
                String vehicleNumber = request.getParameter("vehicleNumber");
                String[] components = request.getParameterValues("components");
                
                if (vehicleNumber != null && components != null) {
                    // Get existing component statuses from session
                    HttpSession session = request.getSession();
                    List<ComponentStatus> existingStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");
                    
                    // Find the vehicle
                    List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
                    Vehicle currentVehicle = vehicles.stream()
                        .filter(v -> v.getVehicleNumber().equals(vehicleNumber))
                        .findFirst()
                        .orElse(null);

                    System.out.println("Processing vehicle: " + vehicleNumber);
                    if (currentVehicle != null) {
                        System.out.println("Last maintenance date: " + currentVehicle.getLastMaintenanceDate());
                    }
                    
                    // Calculate hours used
                    int hoursUsed = calculateHoursUsed(currentVehicle);
                    System.out.println("Hours used calculated: " + hoursUsed);
                    
                    // Calculate wear level based on hours used
                    double wearLevel = 0.0;
                    if (hoursUsed > 0) {
                        // 假設3個月(2160小時)達到100%磨損
                        wearLevel = (hoursUsed / 2160.0) * 100.0;
                        // 限制最大值為100%
                        wearLevel = Math.min(wearLevel, 100.0);
                    }
                    
                    // Create new component statuses
                    List<ComponentStatus> newStatuses = new ArrayList<>();
                    for (String component : components) {
                        ComponentStatus status = new ComponentStatus(vehicleNumber, component, hoursUsed, wearLevel);
                        status.setLastUpdated(LocalDateTime.now());
                        newStatuses.add(status);
                        System.out.println("Created status for " + component + ": hours=" + hoursUsed + ", wear=" + wearLevel);
                    }
                    
                    // Merge existing and new statuses
                    List<ComponentStatus> mergedStatuses = mergeComponentStatuses(existingStatuses, newStatuses);
                    
                    // Store merged statuses in session
                    session.setAttribute("componentStatuses", mergedStatuses);
                    // Also store in request for immediate display
                    request.setAttribute("componentStatuses", mergedStatuses);
                }
                
                // Get vehicle list
                List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
                
                // Check vehicles needing maintenance
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
                
                // Get maintenance tasks
                List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
                
                // Set all necessary attributes
                request.setAttribute("vehicleList", vehicleList);
                request.setAttribute("vehiclesNeedingMaintenance", vehiclesNeedingMaintenance);
                request.setAttribute("scheduledTasks", tasks);
                
                // Forward back to maintenance.jsp
                request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in MaintenanceServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 