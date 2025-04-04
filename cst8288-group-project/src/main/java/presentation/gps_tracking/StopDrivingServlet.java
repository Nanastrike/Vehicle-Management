// StopDrivingServlet.java
package presentation.gps_tracking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/stopDriving")
public class StopDrivingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("isDriving");
        request.getSession().removeAttribute("currentVehicle");
        request.getSession().removeAttribute("isPaused");
        response.sendRedirect("operatorDashboard");
    }
}
