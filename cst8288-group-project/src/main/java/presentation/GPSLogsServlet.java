/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import data.DatabaseConnection;
import data.RouteDao;
import data.RouteDaoImpl;
import data.VehicleActionDTO;
import jakarta.servlet.http.HttpServlet;
import data.VehicleActionDao;
import data.VehicleActionDaoImpl;
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

/**
 *
 * @author silve
 */
@webServlet("/gpsLogs")
public class GPSLogsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDao = new VehicleDAO(conn);
        RouteDao routeDao = new RouteDaoImpl();
        VehicleActionDao vehicleActionDao = new VehicleActionDaoImpl();

        List<Vehicle> allVehicles = vehicleDao.getAllVehicles(); // 所有车辆
        List<TrackingDisplayDTO> displayList = new ArrayList<>();

        for (Vehicle v : allVehicles) {
            int vehicleId = v.getVehicleID();
            int routeId = v.getRouteID();

            // 查路线终点
            String destination = routeDao.getRoadDestinationByID(routeId);

            // 查位置信息（carDistance、时间等）
            VehicleActionDTO log = vehicleActionDao.getVehicleLogs(vehicleId);

            TrackingDisplayDTO dto = new TrackingDisplayDTO();
            dto.setVehicleNubmer(v.getVehicleID());
            dto.setRouteID(routeId);
            dto.setPosition(log != null ? log.getCarDistance() : 0.0);
            dto.setDestination(destination);
            dto.setIs_arrived(log != null && log.getArriveTime() != null ? "Y" : "N");
            dto.setLeavingTime(log != null ? log.getLeavingTime() : null);
            dto.setArriveTime(log != null ? log.getArriveTime() : null);

            displayList.add(dto);
        }

        request.setAttribute("logs", displayList);
        request.getRequestDispatcher("GPSLogs.jsp").forward(request, response);
    }
}
