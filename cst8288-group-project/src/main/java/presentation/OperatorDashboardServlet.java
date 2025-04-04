package presentation;

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
import java.util.List;

@WebServlet("/operatorDashboard")
public class OperatorDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDao = new VehicleDAO(conn);
        List<Vehicle> vehicleList = vehicleDao.getAllVehicles();

        request.setAttribute("vehicleList", vehicleList);
        request.getRequestDispatcher("operator_status.jsp").forward(request, response);
    }
}
