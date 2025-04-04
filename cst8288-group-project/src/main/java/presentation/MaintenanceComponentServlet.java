package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.VehicleManagement.Vehicle;
import view.MaintenanceView;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "MaintenanceComponentServlet", urlPatterns = {"/MaintenanceComponentServlet"})
public class MaintenanceComponentServlet extends HttpServlet {
    private MaintenancePresenter presenter;
    
    @Override
    public void init() throws ServletException {
        try {
            MaintenanceView view = new MaintenanceView(null, null); // Will be updated in each request
            presenter = new MaintenancePresenter(view);
        } catch (SQLException e) {
            throw new ServletException("Error initializing MaintenanceComponentServlet", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Update view with current request/response
            MaintenanceView view = new MaintenanceView(request, response);
            presenter = new MaintenancePresenter(view);
            
            // Get vehicle data
            List<Vehicle> vehicleList = presenter.getAllVehicles();
            List<Vehicle> vehiclesNeedingMaintenance = presenter.getVehiclesNeedingMaintenance();
            
            // Get maintenance tasks
            presenter.displayMaintenanceTasks();
            
            // Set attributes
            request.setAttribute("vehicleList", vehicleList);
            request.setAttribute("vehiclesNeedingMaintenance", vehiclesNeedingMaintenance);
            
            // Display component alerts
            presenter.displayComponentAlerts();
            
            // Forward to maintenance.jsp
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in MaintenanceComponentServlet doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Update view with current request/response
            MaintenanceView view = new MaintenanceView(request, response);
            presenter = new MaintenancePresenter(view);
            
            String action = request.getParameter("action");
            
            switch (action) {
                case "scheduleTask":
                    handleScheduleTask(request);
                    break;
                case "deleteTask":
                    handleDeleteTask(request);
                    break;
                case "checkMechanical":
                    handleMechanicalCheck(request);
                    break;
                case "checkElectrical":
                    handleElectricalCheck(request);
                    break;
                case "checkEngine":
                    handleEngineCheck(request);
                    break;
                case "clearAlerts":
                    presenter.clearAlerts();
                    break;
            }
            
            response.sendRedirect("MaintenanceComponentServlet");
        } catch (Exception e) {
            System.err.println("Error in MaintenanceComponentServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    private void handleScheduleTask(HttpServletRequest request) throws SQLException {
        String vehicleNumber = request.getParameter("vehicleNumber");
        String taskType = request.getParameter("taskType");
        String scheduledDateStr = request.getParameter("scheduledDate");
        String priority = request.getParameter("priority");
        
        LocalDateTime scheduledDate = LocalDateTime.parse(scheduledDateStr + "T00:00:00");
        presenter.scheduleMaintenanceTask(vehicleNumber, taskType, scheduledDate, priority);
    }
    
    private void handleDeleteTask(HttpServletRequest request) throws SQLException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        presenter.deleteMaintenanceTask(taskId);
    }
    
    private void handleMechanicalCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double brakeWear = Double.parseDouble(request.getParameter("brakeWear"));
        double wheelWear = Double.parseDouble(request.getParameter("wheelWear"));
        double bearingWear = Double.parseDouble(request.getParameter("bearingWear"));
        
        presenter.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
    }
    
    private void handleElectricalCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double catenaryWear = Double.parseDouble(request.getParameter("catenaryWear"));
        double pantographWear = Double.parseDouble(request.getParameter("pantographWear"));
        double breakerWear = Double.parseDouble(request.getParameter("breakerWear"));
        
        presenter.monitorElectricalComponents(vehicleId, catenaryWear, pantographWear, breakerWear);
    }
    
    private void handleEngineCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double engineTemp = Double.parseDouble(request.getParameter("engineTemp"));
        double oilPressure = Double.parseDouble(request.getParameter("oilPressure"));
        double fuelEfficiency = Double.parseDouble(request.getParameter("fuelEfficiency"));
        
        presenter.monitorEngineDiagnostics(vehicleId, engineTemp, oilPressure, fuelEfficiency);
    }
} 