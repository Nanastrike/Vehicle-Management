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

@WebServlet("/resumeDriving")
public class ResumeDrivingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Vehicle currentVehicle = (Vehicle) request.getSession().getAttribute("currentVehicle");
        Integer operatorId = (Integer) request.getSession().getAttribute("userID");

        if (currentVehicle != null && operatorId != null) {
            int routeId = currentVehicle.getRouteID();  // ✅ 从 Vehicle 拿路线 ID

            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            double updatedDistance = vehicleAction.vehicleMovedDistance(routeId, operatorId);

            request.getSession().setAttribute("carDistance", updatedDistance);
            request.getSession().setAttribute("isPaused", false);

            if (vehicleAction.isArrived(updatedDistance, routeId)) {
                request.getSession().removeAttribute("isDriving");
                request.getSession().removeAttribute("currentVehicle");
                request.getSession().removeAttribute("carDistance");
                request.getSession().removeAttribute("isPaused");

                // 设置一个提示 flag
                request.getSession().setAttribute("justArrived", true);
            }
        }

        // ✅ 跳转回 dashboard
        response.sendRedirect("operatorDashboard");
    }
}
