package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MaintenanceTask.MaintenanceTask;
import model.MaintenanceTask.ComponentStatus;
import model.MaintenanceTask.VehicleComponentMonitor;
import model.MaintenanceTask.MaintenanceTaskManager;
import view.MaintenanceView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/maintenance")
public class MaintenanceServlet extends HttpServlet {
    private final VehicleComponentMonitor monitor;
    private final MaintenanceTaskManager taskManager;
    
    public MaintenanceServlet() throws SQLException {
        this.monitor = new VehicleComponentMonitor();
        this.taskManager = new MaintenanceTaskManager();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            MaintenanceView view = new MaintenanceView(request, response);
            this.monitor.addObserver(view);
            
            // 獲取並設置維護任務
            List<MaintenanceTask> tasks = taskManager.getAllMaintenanceTasks();
            view.displayMaintenanceTasks(tasks);
            
            // 轉發到 JSP 頁面
            request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error accessing maintenance data", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            MaintenanceView view = new MaintenanceView(request, response);
            this.monitor.addObserver(view);
            
            switch (action) {
                case "checkMechanical":
                    String vehicleId = request.getParameter("vehicleId");
                    monitor.checkMechanicalComponents(
                        Integer.parseInt(vehicleId),
                        Double.parseDouble(request.getParameter("brakeWear")),
                        Double.parseDouble(request.getParameter("wheelWear")),
                        Double.parseDouble(request.getParameter("bearingWear")));
                    break;
                    
                case "checkElectrical":
                    vehicleId = request.getParameter("vehicleId");
                    monitor.checkElectricalComponents(
                        Integer.parseInt(vehicleId),
                        Double.parseDouble(request.getParameter("catenaryWear")),
                        Double.parseDouble(request.getParameter("pantographWear")),
                        Double.parseDouble(request.getParameter("breakerWear")));
                    break;
                    
                case "checkEngine":
                    vehicleId = request.getParameter("vehicleId");
                    monitor.checkEngineDiagnostics(
                        Integer.parseInt(vehicleId),
                        Double.parseDouble(request.getParameter("engineTemp")),
                        Double.parseDouble(request.getParameter("oilPressure")),
                        Double.parseDouble(request.getParameter("fuelEfficiency")));
                    break;
                    
                case "clearAlerts":
                    monitor.clearAlerts();
                    break;
            }
            
            response.sendRedirect(request.getContextPath() + "/maintenance");
        } catch (SQLException e) {
            throw new ServletException("Error processing maintenance request", e);
        }
    }
} 