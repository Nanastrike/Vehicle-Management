// ResumeDrivingServlet.java
package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.VehicleManagement.Vehicle;
import java.io.IOException;
import module.GPS_Tracking.vehicles.VehicleAction;
import module.GPS_Tracking.vehicles.VehicleActionImpl;

@WebServlet("/resumeDriving")
public class ResumeDrivingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ✅ 恢复驾驶状态
        request.getSession().setAttribute("isDriving", true);
        request.getSession().removeAttribute("isPaused");

        // ✅ 获取当前车辆和用户信息
        Vehicle currentVehicle = (Vehicle) request.getSession().getAttribute("currentVehicle");
        Integer operatorId = (Integer) request.getSession().getAttribute("userID");

        if (currentVehicle != null && operatorId != null) {
            int vehicleID = currentVehicle.getVehicleID();
            int routeID = currentVehicle.getRouteID();

            // ✅ 调用业务逻辑继续行驶
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            double newDistance = vehicleAction.vehicleMovedDistance(routeID, operatorId);

            // ✅ 把最新距离也同步回前端展示
            request.getSession().setAttribute("carDistance", newDistance);

            // ✅ 若到达终点，将状态重置
            if (vehicleAction.isArrived(newDistance, routeID)) {
                request.getSession().removeAttribute("isDriving");
                request.getSession().removeAttribute("carDistance");
                request.getSession().removeAttribute("currentVehicle");
            }
        }

        response.sendRedirect("operatorDashboard");
    }
}