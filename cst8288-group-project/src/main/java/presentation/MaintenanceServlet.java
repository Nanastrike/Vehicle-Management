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
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceAlert;
import model.VehicleManagement.Vehicle;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
            
            // Get vehicle list
            List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
            System.out.println("Retrieved vehicle list size: " + (vehicleList != null ? vehicleList.size() : "null"));
            
            if (vehicleList != null) {
                for (Vehicle v : vehicleList) {
                    System.out.println("Vehicle: ID=" + v.getVehicleID() + 
                                     ", Number=" + v.getVehicleNumber() + 
                                     ", Type=" + (v.getVehicleType() != null ? v.getVehicleType().getTypeName() : "null"));
                }
            }
            
            request.setAttribute("vehicleList", vehicleList);
            
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
                    "Maintenance task for vehicle " + vehicleNumber,
                    LocalDateTime.parse(scheduledDate + "T00:00:00"),
                    "System"
                );
                
                response.sendRedirect("MaintenanceServlet");
            } else if ("deleteTask".equals(action)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                taskManager.deleteMaintenanceTask(taskId);
                response.sendRedirect("MaintenanceServlet");
            }
        } catch (Exception e) {
            System.err.println("Error in MaintenanceServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 