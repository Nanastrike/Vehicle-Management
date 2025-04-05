/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.gps_tracking;

/**
 *
 * @author silve
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

import java.io.IOException;

@WebServlet("/pauseDriving")
public class PauseDrivingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Vehicle currentVehicle = (Vehicle) request.getSession().getAttribute("currentVehicle");

        if (currentVehicle != null) {
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            vehicleAction.setRunning(false); // 暂停状态

            request.getSession().setAttribute("isPaused", true);
        }

        response.sendRedirect("operator_status.jsp");
    }
}