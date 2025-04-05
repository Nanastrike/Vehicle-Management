/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.gps_tracking;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

import java.io.IOException;

/**
 * Servlet that handles resuming vehicle driving after being paused.
 * It continues updating the vehicle's position and checks if the destination is reached.
 * If arrived, it clears session data and sets a flag for arrival notification.
 *
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05 
 * @Description: Resumes vehicle operation and updates state accordingly.
 */
@WebServlet("/resumeDriving")
public class ResumeDrivingServlet extends HttpServlet {

    /**
     * Handles POST requests to resume a paused vehicle.
     * Uses session-stored vehicle and operator information to continue movement,
     * updates session attributes, and redirects to the operator dashboard.
     *
     * @param request  the HTTP request containing session data
     * @param response the HTTP response used for redirection
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error is detected
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //  Get current vehicle and operator ID from session
        Vehicle currentVehicle = (Vehicle) request.getSession().getAttribute("currentVehicle");
        Integer operatorId = (Integer) request.getSession().getAttribute("userID");

        if (currentVehicle != null && operatorId != null) {
            int routeId = currentVehicle.getRouteID();  // ✅ Get route ID from vehicle

            //  Resume driving and update distance
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            double updatedDistance = vehicleAction.vehicleMovedDistance(routeId, operatorId);

            //  Update session with new distance and resume state
            request.getSession().setAttribute("carDistance", updatedDistance);
            request.getSession().setAttribute("isPaused", false);

            //  Check if vehicle has arrived after update
            if (vehicleAction.isArrived(updatedDistance, routeId)) {
                request.getSession().removeAttribute("isDriving");
                request.getSession().removeAttribute("currentVehicle");
                request.getSession().removeAttribute("carDistance");
                request.getSession().removeAttribute("isPaused");

                //  Set a flag to show "just arrived" notification on next page
                request.getSession().setAttribute("justArrived", true);
            }
        }

        //  Redirect back to the operator dashboard
        response.sendRedirect("operatorDashboard");
    }
}