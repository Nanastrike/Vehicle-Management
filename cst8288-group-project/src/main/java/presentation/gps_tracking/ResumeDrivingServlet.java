// ResumeDrivingServlet.java
package presentation.gps_tracking;

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
        request.getSession().removeAttribute("isPaused");
        request.getSession().setAttribute("isDriving", true);

        // ✅ 获取当前车辆和用户信息
        Vehicle currentVehicle = (Vehicle) request.getSession().getAttribute("currentVehicle");
        Integer operatorId = (Integer) request.getSession().getAttribute("userID");

        if (currentVehicle != null && operatorId != null) {
            int vehicleID = currentVehicle.getVehicleID();
            int routeID = currentVehicle.getRouteID();

            // ✅ 调用业务逻辑继续行驶
            VehicleAction vehicleAction = new VehicleActionImpl(currentVehicle);
            vehicleAction.setOperatorID(operatorId);  // 需要在 VehicleActionImpl 添加该方法和字段
            vehicleAction.setRunning(true);
            double newDistance = vehicleAction.vehicleMovedDistance(routeID, operatorId);

            // ✅ 把最新距离也同步回前端展示
            request.getSession().setAttribute("carDistance", newDistance);
        }

        response.sendRedirect("operatorDashboard");
    }
}
