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

        if ((refreshParam != null && refreshParam.equals("true")) || vehicleNumberParam == null || vehicleNumberParam.trim().isEmpty()) {
            //  全部车辆 => 显示每辆车最新的一条记录
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
            // 查询某辆车的所有记录
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

        request.setAttribute("logs", displayList);
        request.getRequestDispatcher("gps_tracking.jsp").forward(request, response);
    }
}
