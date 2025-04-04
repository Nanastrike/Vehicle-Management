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

@WebServlet("/operatorDashboard")
public class OperatorDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        VehicleDAO vehicleDAO = new VehicleDAO(conn);
        RouteDao routeDao = new RouteDaoImpl();

        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        request.setAttribute("vehicleList", vehicleList);

        Vehicle currentVehicle = (Vehicle) session.getAttribute("currentVehicle");
        Double carDistance = (Double) session.getAttribute("carDistance");
        Boolean isDriving = (Boolean) session.getAttribute("isDriving");
        Boolean isPaused = (Boolean) session.getAttribute("isPaused");

        if (isDriving == null) isDriving = false;
        if (isPaused == null) isPaused = false;
        if (carDistance == null) carDistance = 0.0;

        boolean isArrived = false;
        if (isDriving && !isPaused && currentVehicle != null) {
            int routeID = currentVehicle.getRouteID();
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            isArrived = vehicleAction.isArrived(carDistance, routeID);

            if (isArrived) {
                session.removeAttribute("isDriving");
                session.removeAttribute("isPaused");
                session.removeAttribute("currentVehicle");
                session.removeAttribute("carDistance");

                session.setAttribute("justArrived", true);
            }
        }

        // ✅ 读取 justArrived flag 给 JSP 提示用
        Boolean justArrived = (Boolean) session.getAttribute("justArrived");
        if (justArrived != null && justArrived) {
            request.setAttribute("justArrived", true);
            session.removeAttribute("justArrived");
        }

        request.setAttribute("isDriving", isDriving);
        request.setAttribute("isPaused", isPaused);
        request.setAttribute("carDistance", carDistance);
        request.setAttribute("isArrived", isArrived);

        request.getRequestDispatcher("operator_status.jsp").forward(request, response);
    }
}
