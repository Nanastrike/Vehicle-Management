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

@WebServlet("/startDriving")
public class StartDrivingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vehicleIdStr = request.getParameter("vehicleId");

        if (vehicleIdStr != null) {
            int vehicleId = Integer.parseInt(vehicleIdStr);

            Connection conn = DatabaseConnection.getInstance().getConnection();
            VehicleDAO vehicleDAO = new VehicleDAO(conn);
            Vehicle vehicle = vehicleDAO.getVehicleByID(vehicleId);

            if (vehicle != null) {
                request.getSession().setAttribute("isDriving", true);
                request.getSession().setAttribute("currentVehicle", vehicle);
                request.getSession().removeAttribute("isPaused");

                System.out.println("✔ StartDrivingServlet: selected " + vehicle.getVehicleNumber());
            } else {
                System.out.println("⚠ vehicleID not found: " + vehicleId);
            }
        }

        response.sendRedirect("operatorDashboard");
    }
}
