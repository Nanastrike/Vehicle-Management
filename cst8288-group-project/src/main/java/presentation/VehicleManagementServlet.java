package presentation;

import data.VehicleDAO;
import data.MaintenanceTaskDAO;
import model.VehicleManagement.*;
import data.DatabaseConnection;
import data.DashboardDAO;
import data.gps_tracking.VehicleActionDTO;
import data.gps_tracking.VehicleActionDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.MaintenanceTask.MaintenanceTask;

/**
 * VehicleManagementServlet handles all vehicle-related actions such as listing,
 * inserting, updating, and dashboard data preparation within the Public Transit Fleet Management System.
 * <p>
 * It interacts with {@link data.VehicleDAO} for database operations and uses
 * {@link data.DashboardDAO} for dashboard-specific statistics.
 * </p>
 *
 * <h3>Supported Actions:</h3>
 * <ul>
 *     <li><code>?action=list</code> – List all vehicles</li>
 *     <li><code>?action=insert</code> – Insert a new vehicle</li>
 *     <li><code>?action=update</code> – Update existing vehicle data</li>
 *     <li><code>?action=dashboard</code> – Load dashboard data (vehicle stats)</li>
 * </ul>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 * @see jakarta.servlet.http.HttpServlet
 * @see data.VehicleDAO
 * @see data.DashboardDAO
 * @see model.VehicleManagement.Vehicle
 */
public class VehicleManagementServlet extends HttpServlet {

    private Connection conn;
    private VehicleDAO vehicleDAO;
    private MaintenanceTaskDAO maintenanceTaskDAO;
    private VehicleActionDaoImpl vehicleActionDaoImpl;

    /**
     * Initializes the servlet and sets up database connection and DAO instance.
     */
    @Override
    public void init() throws ServletException {
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            vehicleDAO = new VehicleDAO(conn);
            maintenanceTaskDAO = new MaintenanceTaskDAO(conn);
            vehicleActionDaoImpl = new VehicleActionDaoImpl(conn);
        } catch (Exception e) {
            throw new ServletException("Error initializing VehicleManagementServlet", e);
        }
    }

    /**
     * Handles GET requests to list vehicles or display the dashboard view.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            listVehicles(request, response);
        } else if (action.equals("dashboard")) {
            DashboardDAO dashboardDAO = new DashboardDAO(vehicleDAO, maintenanceTaskDAO, vehicleActionDaoImpl);
            try {
                Map<String, Integer> vehicleTypeCounts = dashboardDAO.getVehicleTypeCounts();
                Vehicle lastVehicle = dashboardDAO.getLastAddedVehicle();
                Integer highPriorityCount = dashboardDAO.getHighPriorityTaskCount();
                MaintenanceTask mostRecentTask = dashboardDAO.getMostRecentTask();
                VehicleActionDaoImpl gpsDao = new VehicleActionDaoImpl(conn);
                int runningCount = gpsDao.getRunningVehiclesCount();
                List<VehicleActionDTO> recentVehicles = gpsDao.getRecentVehicleActions(3);

                request.setAttribute("runningVehicleCount", runningCount);
                request.setAttribute("recentVehicles", recentVehicles);

                request.setAttribute("vehicleTypeCounts", vehicleTypeCounts);
                request.setAttribute("lastVehicle", lastVehicle);
                request.setAttribute("highPriorityCount", highPriorityCount);
                request.setAttribute("mostRecentTask", mostRecentTask);

                request.getRequestDispatcher("DashboardPage.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading dashboard data.");
            }

        } else if (action.equals("update")) {
            showEditForm(request, response);
        } else {
            response.sendRedirect("DashboardPage.jsp");
        }
    }



    /**
     * Handles POST requests for inserting or updating vehicles.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("insert")) {
            insertVehicle(request, response);
        } else if (action.equals("update")) {
            updateVehicle(request, response);
        }
    }

    /**
     * Lists all vehicles and forwards the data to VehiclesPage.jsp.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void listVehicles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        request.setAttribute("vehicleList", vehicleList);
        request.getRequestDispatcher("VehiclesPage.jsp").forward(request, response);
    }

    /**
     * Forwards request to the vehicle addition page.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
    }

    /**
     * Handles insertion of a new vehicle into the database.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if input is invalid or error occurs
     */
    private void insertVehicle(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            if (vehicleNumber == null || vehicleNumber.isEmpty()) {
                request.setAttribute("error", "Vehicle Number is required.");
                request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
                return;
            }

            int vehicleTypeID = Integer.parseInt(request.getParameter("vehicleTypeID"));
            int fuelTypeID = Integer.parseInt(request.getParameter("fuelTypeID"));
            float consumptionRate = Float.parseFloat(request.getParameter("consumptionRate"));
            int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
            int routeID = request.getParameter("routeID") != null && !request.getParameter("routeID").isEmpty()
                    ? Integer.parseInt(request.getParameter("routeID")) : 0;
            Date lastMaintenanceDate = Date.valueOf(request.getParameter("lastMaintenanceDate"));

            VehicleType vehicleType = new VehicleType(vehicleTypeID, getVehicleTypeName(vehicleTypeID));
            FuelType fuelType = new FuelType(fuelTypeID, getFuelTypeName(fuelTypeID));
            Vehicle newVehicle = new Vehicle(0, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);

            if (vehicleDAO.isVehicleNumberExists(vehicleNumber)) {
                request.setAttribute("error", "Vehicle number already exists.");
                request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
                return;
            }

            if (vehicleDAO.addVehicle(newVehicle)) {
                response.sendRedirect("VehicleManagementServlet?action=list");
            } else {
                request.setAttribute("error", "Failed to add vehicle.");
                request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input.");
            request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
        }
    }

    /**
     * Forwards to edit vehicle form with pre-filled data.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
        Vehicle existingVehicle = vehicleDAO.getVehicleByID(vehicleID);
        request.setAttribute("vehicle", existingVehicle);
        request.getRequestDispatcher("EditVehiclePage.jsp").forward(request, response);
    }

    /**
     * Updates an existing vehicle’s information in the database.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an I/O error occurs
     */
    private void updateVehicle(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        try {
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            String vehicleNumber = request.getParameter("vehicleNumber");
            int vehicleTypeID = Integer.parseInt(request.getParameter("vehicleTypeID"));
            int fuelTypeID = Integer.parseInt(request.getParameter("fuelTypeID"));
            float consumptionRate = Float.parseFloat(request.getParameter("consumptionRate"));
            int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
            int routeID = Integer.parseInt(request.getParameter("routeID"));
            Date lastMaintenanceDate = Date.valueOf(request.getParameter("lastMaintenanceDate"));

            VehicleType vehicleType = new VehicleType(vehicleTypeID, getVehicleTypeName(vehicleTypeID));
            FuelType fuelType = new FuelType(fuelTypeID, getFuelTypeName(fuelTypeID));

            Vehicle updatedVehicle = new Vehicle(vehicleID, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);

            if (vehicleDAO.updateVehicle(updatedVehicle)) {
                response.sendRedirect("VehicleManagementServlet?action=list");
            } else {
                response.sendRedirect("EditVehiclePage.jsp?vehicleID=" + vehicleID + "&error=Failed to update vehicle.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("EditVehiclePage.jsp?error=Invalid+input");
        }
    }

    /**
     * Deletes a vehicle based on the provided vehicle ID.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an I/O error occurs
     */
    private void deleteVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
        vehicleDAO.deleteVehicle(vehicleID);
        response.sendRedirect("VehicleManagementServlet?action=list");
    }

    /**
     * Maps a vehicle type ID to its name.
     *
     * @param vehicleTypeID the vehicle type ID
     * @return the corresponding vehicle type name
     */
    private String getVehicleTypeName(int vehicleTypeID) {
        switch (vehicleTypeID) {
            case 1: return "Diesel Bus";
            case 2: return "Electric Light Rail";
            case 3: return "Diesel-Electric Train";
            default: return "Unknown";
        }
    }

    /**
     * Maps a fuel type ID to its name.
     *
     * @param fuelTypeID the fuel type ID
     * @return the corresponding fuel type name
     */
    private String getFuelTypeName(int fuelTypeID) {
        switch (fuelTypeID) {
            case 1: return "Diesel";
            case 2: return "CNG";
            case 3: return "Electric";
            default: return "Unknown";
        }
    }
}
