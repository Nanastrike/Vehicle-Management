/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.gps_tracking;

import data.DatabaseConnection;
import data.VehicleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.VehicleManagement.Vehicle;

import java.io.IOException;
import java.sql.Connection;
import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

/**
 * Servlet that handles the initial "Start Driving" request.
 * When an operator selects a vehicle and route, this servlet initializes the vehicle's
 * driving session, updates its movement status, and stores key information in the session.
 *
 * This differs from ResumeDrivingServlet as it represents the first time a trip starts,
 * not a continuation.
 *
 * @author : Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: Group Project
 * @time: 2025/04/05
 * @Description: Initializes vehicle trip state and starts a new session for tracking.
 */
@WebServlet("/startDriving")
public class StartDrivingServlet extends HttpServlet {

    /**
     * Handles POST requests to begin a new vehicle trip.Extracts selected vehicle and route, creates a VehicleAction to simulate movement,
 and sets all relevant session attributes before redirecting to the dashboard.
     *
     * @param request  the HTTP request with vehicle/route parameters
     * @param response the HTTP response used to redirect after setting session
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //  Get form parameters
        String vehicleIdStr = request.getParameter("vehicleId");
        String routeIdStr = request.getParameter("routeId");

        //  Debugging: Print received parameters to console
        System.out.println("vehicleId received: " + vehicleIdStr);
        System.out.println("routeId received: " + routeIdStr);

        //  Convert string parameters to integers
        int vehicleID = Integer.parseInt(vehicleIdStr);
        // ️ You hardcoded routeID = 1 — consider fixing if route selection is dynamic
        // int routeID = Integer.parseInt(routeIdStr);
        int routeID = 1;

        //  Get operator ID from session
        int operatorID = (int) request.getSession().getAttribute("userID");

        //  Fetch vehicle object from database
        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDAO = new VehicleDAO(conn);
        Vehicle vehicle = vehicleDAO.getVehicleByID(vehicleID);

        //  Simulate driving — calculate initial car distance
        VehicleAction action = new VehicleActionImpl(vehicle);
        double carDistance = action.vehicleMovedDistance(routeID, operatorID);

        //  Store driving status in session
        request.getSession().setAttribute("isDriving", true);
        request.getSession().removeAttribute("isPaused");        // Clear pause state if present
        request.getSession().setAttribute("currentVehicle", vehicle);
        request.getSession().setAttribute("carDistance", carDistance);
        request.getSession().removeAttribute("tripCompleted");   // Clear old trip state if any

        //  Redirect to operator dashboard
        response.sendRedirect("operatorDashboard");
    }
}
