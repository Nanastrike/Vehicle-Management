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
            } else {
                // Handle component status update
                String vehicleNumber = request.getParameter("vehicleNumber");
                String[] components = request.getParameterValues("components");
                
                if (vehicleNumber != null && components != null) {
                    // Get existing component statuses from session
                    HttpSession session = request.getSession();
                    List<ComponentStatus> existingStatuses = (List<ComponentStatus>) session.getAttribute("componentStatuses");
                    
                    // Create new component statuses
                    List<ComponentStatus> newStatuses = new ArrayList<>();
                    for (String component : components) {
                        ComponentStatus status = new ComponentStatus(vehicleNumber, component, 0, 0);
                        status.setLastUpdated(LocalDateTime.now());
                        newStatuses.add(status);
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