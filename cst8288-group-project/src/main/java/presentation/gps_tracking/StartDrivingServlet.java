/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.gps_tracking;

/**
 *
 * @author silve
 */
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

@WebServlet("/startDriving")
public class StartDrivingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vehicleIdStr = request.getParameter("vehicleId");
        String routeIdStr = request.getParameter("routeId");

        System.out.println(" vehicleId 参数收到的值: " + vehicleIdStr);
        System.out.println("️ routeId : " + routeIdStr);

        int vehicleID = Integer.parseInt(request.getParameter("vehicleId"));
//        int routeID = Integer.parseInt(request.getParameter("routeId"));
        int routeID = 1;
        int operatorID = (int) request.getSession().getAttribute("userID");

        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDAO = new VehicleDAO(conn);
        Vehicle vehicle = vehicleDAO.getVehicleByID(vehicleID);

        VehicleAction action = new VehicleActionImpl(vehicle);
        double carDistance = action.vehicleMovedDistance(routeID, operatorID);

        // 设置 session 状态
        request.getSession().setAttribute("isDriving", true);
        request.getSession().removeAttribute("isPaused");
        request.getSession().setAttribute("currentVehicle", vehicle);
        request.getSession().setAttribute("carDistance", carDistance);
        request.getSession().removeAttribute("tripCompleted");

        response.sendRedirect("operatorDashboard");
    }
}
