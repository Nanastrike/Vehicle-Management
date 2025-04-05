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
import data.gps_tracking.RouteDao;
import data.gps_tracking.RouteDaoImpl;
import data.gps_tracking.VehicleActionDTO;
import jakarta.servlet.http.HttpServlet;
import data.gps_tracking.VehicleActionDao;
import data.gps_tracking.VehicleActionDaoImpl;
import data.VehicleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.VehicleManagement.Vehicle;
import module.GPS_Tracking.TrackingDisplayDTO;
import jakarta.servlet.annotation.WebServlet;

/**
 * Servlet that handles requests to display GPS tracking logs for vehicles.
 * Depending on query parameters, it either returns the latest log for all vehicles
 * or the full tracking history for a specific vehicle.
 *
 * @author :Qinyu Luo
 * @version: 1.0
 * @course: CST8288
 * @assignment: group project
 * @time: 2025/04/05
 * Description: Handles display logic for GPS tracking logs, fetching vehicle,
 * operator, and route data, and passing it to the frontend for presentation.
 */
@WebServlet("/gpsLogs")
public class GPSLogsServlet extends HttpServlet {

    /**
     * Handles GET requests for GPS tracking logs.
     * Accepts optional parameters:
     * - vehicleNumber: to fetch logs for a specific vehicle
     * - refresh=true: to fetch the latest record for each vehicle
     *
     * @param request  the HTTP request object containing query parameters
     * @param response the HTTP response object to forward data to JSP
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize DAOs and database connection
        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDao = new VehicleDAO(conn);
        VehicleActionDao vehicleActionDao = new VehicleActionDaoImpl();
        RouteDao routeDao = new RouteDaoImpl();

        // Retrieve query parameters
        String vehicleNumberParam = request.getParameter("vehicleNumber");
        String refreshParam = request.getParameter("refresh");

        List<TrackingDisplayDTO> displayList = new ArrayList<>();

        // === Case 1: No vehicle specified OR refresh triggered => show latest record for all vehicles ===
        if ((refreshParam != null && refreshParam.equals("true")) ||
                vehicleNumberParam == null || vehicleNumberParam.trim().isEmpty()) {

            List<VehicleActionDTO> logs = vehicleActionDao.getAllVehicleLogs();

            for (VehicleActionDTO log : logs) {
                Vehicle vehicle = vehicleDao.getVehicleByID(log.getVehicleID());

                if (vehicle != null) {
                    TrackingDisplayDTO dto = new TrackingDisplayDTO();
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRouteID(vehicle.getRouteID());
                    dto.setDestination(routeDao.getRoadDestinationByID(vehicle.getRouteID()));
                    dto.setPosition(log.getCarDistance());
                    dto.setLeavingTime(log.getLeavingTime());
                    dto.setArriveTime(log.getArriveTime());
                    dto.setIs_arrived(log.getArriveTime() != null ? "Y" : "N");
                    dto.setOperatorName(log.getOperatorName());
                    displayList.add(dto);
                }
            }

        } else {
            // === Case 2: A specific vehicle is selected => show all logs for that vehicle ===
            Vehicle vehicle = vehicleDao.getVehicleByNumber(vehicleNumberParam.trim());

            if (vehicle != null) {
                List<VehicleActionDTO> logs = vehicleActionDao.getAllLogsByVehicleID(vehicle.getVehicleID());

                for (VehicleActionDTO log : logs) {
                    TrackingDisplayDTO dto = new TrackingDisplayDTO();
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRouteID(vehicle.getRouteID());
                    dto.setDestination(routeDao.getRoadDestinationByID(vehicle.getRouteID()));
                    dto.setPosition(log.getCarDistance());
                    dto.setLeavingTime(log.getLeavingTime());
                    dto.setArriveTime(log.getArriveTime());
                    dto.setIs_arrived(log.getArriveTime() != null ? "Y" : "N");
                    dto.setOperatorName(log.getOperatorName());
                    displayList.add(dto);
                }
            }
        }

        // Pass the log list to the JSP for rendering
        request.setAttribute("logs", displayList);
        request.getRequestDispatcher("gps_tracking.jsp").forward(request, response);
    }
}