/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.gps_tracking;

import data.DatabaseConnection;
import data.VehicleDAO;
import data.gps_tracking.RouteDao;
import data.gps_tracking.RouteDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.VehicleManagement.Vehicle;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

/**
 * Servlet that handles rendering of the operator dashboard. It provides vehicle
 * information for dropdowns, retrieves driving status from session, and
 * prepares values for display in the dashboard page.
 * 
 * Description: Displays the operator interface for vehicle
 * selection and status tracking.
 * 
 * 
 * @author Qinyu Luo
 * @version 1.0
 * @since 2025/04/05
 * 
 *
 */
@WebServlet("/operatorDashboard")
public class OperatorDashboardServlet extends HttpServlet {

    /**
     * Handles GET requests to load the operator dashboard. Retrieves vehicle
     * list for dropdown selection, extracts session state for current vehicle,
     * and prepares default values to avoid null pointer errors.
     *
     * @param request the HTTP request containing session or parameters
     * @param response the HTTP response for forwarding data to the JSP
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        VehicleDAO vehicleDAO = new VehicleDAO(conn);
        RouteDao routeDao = new RouteDaoImpl();

        //  1. Retrieve list of all vehicles for dropdown menu display
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        request.setAttribute("vehicleList", vehicleList);

        //  2. Retrieve current session state variables
        Vehicle currentVehicle = (Vehicle) session.getAttribute("currentVehicle");
        Double carDistance = (Double) session.getAttribute("carDistance");
        Boolean isDriving = (Boolean) session.getAttribute("isDriving");
        Boolean isPaused = (Boolean) session.getAttribute("isPaused");

        //  3. Null safety checks and default values
        if (isDriving == null) {
            isDriving = false;
        }
        if (isPaused == null) {
            isPaused = false;
        }
        if (carDistance == null) {
            carDistance = 0.0;
        }

        //  4. Prepare state variable for arrival status
        boolean isArrived = false;

        //  3. Check if vehicle is running and not paused — determine if destination is reached
        if (isDriving && !isPaused && currentVehicle != null) {
            int routeID = currentVehicle.getRouteID();
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            isArrived = vehicleAction.isArrived(carDistance, routeID);

            if (isArrived) {
                // Clear session states to reset dashboard after arrival
                session.removeAttribute("isDriving");
                session.removeAttribute("isPaused");
                session.removeAttribute("currentVehicle");
                session.removeAttribute("carDistance");

                // Mark that vehicle has just arrived — used to display message
                session.setAttribute("justArrived", true);
            }
        }

        //  4. If vehicle has just arrived, notify JSP to display a message
        Boolean justArrived = (Boolean) session.getAttribute("justArrived");
        if (justArrived != null && justArrived) {
            request.setAttribute("justArrived", true);   // Used in JSP
            session.removeAttribute("justArrived");      // Prevent message from reappearing
        }

        //  5. Send vehicle state variables to JSP for UI display
        request.setAttribute("isDriving", isDriving);
        request.setAttribute("isPaused", isPaused);
        request.setAttribute("carDistance", carDistance);
        request.setAttribute("isArrived", isArrived);

        //  6. Forward the request to the operator dashboard JSP
        request.getRequestDispatcher("operator_status.jsp").forward(request, response);
    }
}
