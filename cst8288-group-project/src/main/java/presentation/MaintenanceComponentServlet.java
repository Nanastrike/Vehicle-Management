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

/**
 * MaintenanceComponentServlet handles HTTP requests related to vehicle component maintenance.
 * It interacts with the presenter and view layers to manage tasks, alerts, and diagnostics
 * for various vehicle systems such as mechanical, electrical, and engine components.
 *
 * <p>Supported operations:</p>
 * <ul>
 *     <li>Display vehicles and maintenance alerts</li>
 *     <li>Schedule or delete tasks</li>
 *     <li>Monitor mechanical, electrical, and engine components</li>
 *     <li>Clear alerts</li>
 * </ul>
 *
 * @author Yen-Yi Hsu
 * @version 1.0
 * @since 1.21
 */
@WebServlet(name = "MaintenanceComponentServlet", urlPatterns = {"/MaintenanceComponentServlet"})
public class MaintenanceComponentServlet extends HttpServlet {
    private MaintenancePresenter presenter;

    /**
     * Initializes the presenter with a placeholder view.
     *
     * @throws ServletException if database connection fails during initialization
     */
    @Override
    public void init() throws ServletException {
        try {
            // Dummy view initialized here; real view injected in each request
            MaintenanceView view = new MaintenanceView(null, null);
            presenter = new MaintenancePresenter(view);
        } catch (SQLException e) {
            throw new ServletException("Error initializing MaintenanceComponentServlet", e);
        }
    }

    /**
     * Handles HTTP GET requests by fetching vehicle and maintenance task data,
     * then forwarding it to the JSP view for display.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Create a view with current request/response context
            MaintenanceView view = new MaintenanceView(request, response);
            presenter = new MaintenancePresenter(view);

            // Retrieve all vehicles and those needing maintenance
            List<Vehicle> vehicleList = presenter.getAllVehicles();
            List<Vehicle> vehiclesNeedingMaintenance = presenter.getVehiclesNeedingMaintenance();

            // Display maintenance tasks and component alerts
            presenter.displayMaintenanceTasks();
            presenter.displayComponentAlerts();

            // Attach data to the request for JSP rendering
            request.setAttribute("vehicleList", vehicleList);
            request.setAttribute("vehiclesNeedingMaintenance", vehiclesNeedingMaintenance);

            // Forward request to JSP page
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in MaintenanceComponentServlet doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Handles HTTP POST requests, such as scheduling/deleting tasks or checking components.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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

            // Redirect back to servlet to reflect updated data
            response.sendRedirect("MaintenanceComponentServlet");
        } catch (Exception e) {
            System.err.println("Error in MaintenanceComponentServlet doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Schedules a new maintenance task based on user input.
     *
     * @param request the request containing form data
     * @throws SQLException if task scheduling fails
     */
    private void handleScheduleTask(HttpServletRequest request) throws SQLException {
        String vehicleNumber = request.getParameter("vehicleNumber");
        String taskType = request.getParameter("taskType");
        String scheduledDateStr = request.getParameter("scheduledDate");
        String priority = request.getParameter("priority");

        // Parse the input date (assumed format: yyyy-MM-dd)
        LocalDateTime scheduledDate = LocalDateTime.parse(scheduledDateStr + "T00:00:00");
        presenter.scheduleMaintenanceTask(vehicleNumber, taskType, scheduledDate, priority);
    }

    /**
     * Deletes an existing maintenance task by ID.
     *
     * @param request the request containing the task ID
     * @throws SQLException if deletion fails
     */
    private void handleDeleteTask(HttpServletRequest request) throws SQLException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        presenter.deleteMaintenanceTask(taskId);
    }

    /**
     * Handles mechanical component diagnostics and updates their statuses.
     *
     * @param request HTTP request with wear values
     */
    private void handleMechanicalCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double brakeWear = Double.parseDouble(request.getParameter("brakeWear"));
        double wheelWear = Double.parseDouble(request.getParameter("wheelWear"));
        double bearingWear = Double.parseDouble(request.getParameter("bearingWear"));

        presenter.monitorMechanicalComponents(vehicleId, brakeWear, wheelWear, bearingWear);
    }

    /**
     * Handles electrical component diagnostics and updates their statuses.
     *
     * @param request HTTP request with wear values
     */
    private void handleElectricalCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double catenaryWear = Double.parseDouble(request.getParameter("catenaryWear"));
        double pantographWear = Double.parseDouble(request.getParameter("pantographWear"));
        double breakerWear = Double.parseDouble(request.getParameter("breakerWear"));

        presenter.monitorElectricalComponents(vehicleId, catenaryWear, pantographWear, breakerWear);
    }

    /**
     * Handles engine diagnostics and updates their statuses.
     *
     * @param request HTTP request with temperature, pressure, and efficiency data
     */
    private void handleEngineCheck(HttpServletRequest request) {
        String vehicleId = request.getParameter("vehicleId");
        double engineTemp = Double.parseDouble(request.getParameter("engineTemp"));
        double oilPressure = Double.parseDouble(request.getParameter("oilPressure"));
        double fuelEfficiency = Double.parseDouble(request.getParameter("fuelEfficiency"));

        presenter.monitorEngineDiagnostics(vehicleId, engineTemp, oilPressure, fuelEfficiency);
    }
}
