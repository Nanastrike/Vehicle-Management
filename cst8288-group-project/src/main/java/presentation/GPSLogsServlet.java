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
import jakarta.servlet.annotation.WebServlet;

/**
 *
 * @author silve
 */
@WebServlet("/gpsLogs")
public class GPSLogsServlet extends HttpServlet {

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        VehicleDAO vehicleDao = new VehicleDAO(conn);
        VehicleActionDao vehicleActionDao = new VehicleActionDaoImpl();
        RouteDao routeDao = new RouteDaoImpl();

        String vehicleNumberParam = request.getParameter("vehicleNumber");
        String refreshParam = request.getParameter("refresh");

        List<TrackingDisplayDTO> displayList = new ArrayList<>();

        // ✅ 如果用户点击了 Refresh，或者没输入任何编号，就显示所有车辆
        if ((refreshParam != null && refreshParam.equals("true")) || vehicleNumberParam == null || vehicleNumberParam.trim().isEmpty()) {
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
                    displayList.add(dto);
                }
            }
        } else {
            // ✅ 有输入编号，只查这辆车
            Vehicle vehicle = vehicleDao.getVehicleByNumber(vehicleNumberParam.trim());
            if (vehicle != null) {
                VehicleActionDTO log = vehicleActionDao.getVehicleLogs(vehicle.getVehicleID());
                if (log != null) {
                    TrackingDisplayDTO dto = new TrackingDisplayDTO();
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRouteID(vehicle.getRouteID());
                    dto.setDestination(routeDao.getRoadDestinationByID(vehicle.getRouteID()));
                    dto.setPosition(log.getCarDistance());
                    dto.setLeavingTime(log.getLeavingTime());
                    dto.setArriveTime(log.getArriveTime());
                    dto.setIs_arrived(log.getArriveTime() != null ? "Y" : "N");
                    displayList.add(dto);
                }
            }
        }

        request.setAttribute("logs", displayList);
        request.getRequestDispatcher("gps_tracking.jsp").forward(request, response);
    }
}
