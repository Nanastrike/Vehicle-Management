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

        // 获取所有车辆列表用于下拉选择
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        request.setAttribute("vehicleList", vehicleList);

        Vehicle currentVehicle = (Vehicle) session.getAttribute("currentVehicle");
        Double carDistance = (Double) session.getAttribute("carDistance");
        Boolean isDriving = (Boolean) session.getAttribute("isDriving");
        Boolean isPaused = (Boolean) session.getAttribute("isPaused");

        // 默认值处理
        if (isDriving == null) isDriving = false;
        if (isPaused == null) isPaused = false;
        if (carDistance == null) carDistance = 0.0;

        // 如果已经开车，判断是否到达终点
        boolean isArrived = false;
        if (isDriving && !isPaused && currentVehicle != null) {
            int routeID = currentVehicle.getRouteID();
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            isArrived = vehicleAction.isArrived(carDistance, routeID);

            // 如果已经到达终点，则清空状态，回首页状态
            if (isArrived) {
                session.removeAttribute("isDriving");
                session.removeAttribute("isPaused");
                session.removeAttribute("currentVehicle");
                session.removeAttribute("carDistance");
                // 可以设置一个提示用变量
                request.setAttribute("justArrived", true);
            }
        }

        // 设置状态传给前端
        request.setAttribute("isDriving", isDriving);
        request.setAttribute("isPaused", isPaused);
        request.setAttribute("carDistance", carDistance);
        request.setAttribute("isArrived", isArrived);

        request.getRequestDispatcher("operator_status.jsp").forward(request, response);
    }
}
