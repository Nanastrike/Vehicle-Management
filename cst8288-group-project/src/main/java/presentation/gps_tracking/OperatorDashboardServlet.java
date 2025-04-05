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

        // ✅ 1. 获取所有车辆列表用于下拉框展示
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        request.setAttribute("vehicleList", vehicleList);

        // ✅ 2. 取出 session 中的状态变量
        Vehicle currentVehicle = (Vehicle) session.getAttribute("currentVehicle");
        Double carDistance = (Double) session.getAttribute("carDistance");
        Boolean isDriving = (Boolean) session.getAttribute("isDriving");
        Boolean isPaused = (Boolean) session.getAttribute("isPaused");

        // 防空处理
        if (isDriving == null) isDriving = false;
        if (isPaused == null) isPaused = false;
        if (carDistance == null) carDistance = 0.0;

        boolean isArrived = false;

        // ✅ 3. 如果在开车状态，判断是否已经到达终点
        if (isDriving && !isPaused && currentVehicle != null) {
            int routeID = currentVehicle.getRouteID();
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            isArrived = vehicleAction.isArrived(carDistance, routeID);

            if (isArrived) {
                // 清除行驶状态，回到初始界面
                session.removeAttribute("isDriving");
                session.removeAttribute("isPaused");
                session.removeAttribute("currentVehicle");
                session.removeAttribute("carDistance");

                // 标记终点到达
                session.setAttribute("justArrived", true);
            }
        }

        // ✅ 4. 如果刚到达终点，则展示提示
        Boolean justArrived = (Boolean) session.getAttribute("justArrived");
        if (justArrived != null && justArrived) {
            request.setAttribute("justArrived", true);  // 给 JSP 使用
            session.removeAttribute("justArrived");     // 清除避免重复
        }

        // ✅ 5. 设置 JSP 页面需要的状态
        request.setAttribute("isDriving", isDriving);
        request.setAttribute("isPaused", isPaused);
        request.setAttribute("carDistance", carDistance);
        request.setAttribute("isArrived", isArrived);

        // ✅ 6. 转发到操作员页面
        request.getRequestDispatcher("operator_status.jsp").forward(request, response);
    }
}