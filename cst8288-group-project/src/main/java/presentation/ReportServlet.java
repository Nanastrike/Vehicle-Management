package presentation;

import data.reportDAO;
import model.Report.Report;
import Fuel_dao.FuelConsumptionDAO;
import data.MaintenanceTaskDAO;
import data.DatabaseConnection;
import model.MaintenanceTask.MaintenanceTask;
import Fuel_model.FuelConsumption;
import data.gps_tracking.VehicleActionDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import model.User.User;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
    private Connection conn;
    private MaintenanceTaskDAO maintenanceDAO;
    private FuelConsumptionDAO fuelDAO;
    private reportDAO reportDao;
    private VehicleActionDaoImpl vehicleActionDaoImpl;
    
    @Override
    public void init() throws ServletException {
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            maintenanceDAO = new MaintenanceTaskDAO(conn);
            fuelDAO = new FuelConsumptionDAO();
            reportDao = new reportDAO(conn);
            vehicleActionDaoImpl = new VehicleActionDaoImpl();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ReportServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        String type = request.getParameter("type");  // "maintenance", "fuel", "operator"
        String reportContent = "";
        String reportTitle = "";

        try {
            if ("maintenance".equalsIgnoreCase(type)) {
                reportTitle = "Maintenance Report";
                List<MaintenanceTask> tasks = maintenanceDAO.getPendingTasks();
                StringBuilder sb = new StringBuilder();
                for (MaintenanceTask task : tasks) {
                    sb.append("TaskID: ").append(task.getTaskId())
                      .append(", VehicleID: ").append(task.getVehicleId())
                      .append(", Type: ").append(task.getTaskType())
                      .append(", Date: ").append(task.getScheduledDate())
                      .append(", Priority: ").append(task.getPriority())
                      .append(", Status: ").append(task.getStatus())
                      .append("\n");
                }
                reportContent = sb.toString();

            } else if ("fuel".equalsIgnoreCase(type)) {
                reportTitle = "Fuel Efficiency Report";
                List<FuelConsumption> records = fuelDAO.getAllFuelConsumption();
                StringBuilder sb = new StringBuilder();
                for (FuelConsumption fc : records) {
                    float rate = fc.getDistanceTraveled() != 0 
                                ? (fc.getFuelUsed() / fc.getDistanceTraveled()) * 100 
                                : 0;
                    sb.append("VehicleID: ").append(fc.getVehicleId())
                      .append(", Fuel Used: ").append(fc.getFuelUsed())
                      .append(", Distance: ").append(fc.getDistanceTraveled())
                      .append(", Rate: ").append(String.format("%.2f", rate)).append(" L/100km")
                      .append(", Status: ").append(fc.getStatus())
                      .append("\n");
                }
                reportContent = sb.toString();

            } else if ("operator".equalsIgnoreCase(type)) {
            reportTitle = "Operator Efficiency Report";
            Map<String, Double> operatorEfficiency = vehicleActionDaoImpl.calculateOperatorEfficiency();

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Double> entry : operatorEfficiency.entrySet()) {
                sb.append("Operator: ").append(entry.getKey())
                  .append(", Total Distance: ").append(String.format("%.2f", entry.getValue())).append(" km\n");
            }
            reportContent = sb.toString();
            }else {
                request.setAttribute("error", "Invalid report type.");
                request.getRequestDispatcher("reportPage.jsp").forward(request, response);
                return;
            }

            // Save the report
            Report report = new Report();
            report.setTitle(reportTitle);
            report.setContent(reportContent);
            report.setReportType(type); 
            report.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            report.setCreatedBy(user != null ? user.getName() : "System");
            reportDao.insertReport(report);

            request.setAttribute("success", reportTitle + " generated and saved successfully.");
            request.setAttribute("reportTitle", reportTitle);
            request.setAttribute("reportContent", reportContent);
            request.getRequestDispatcher("reportPage.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error generating report: " + e.getMessage());
            request.getRequestDispatcher("reportPage.jsp").forward(request, response);
        }
    }
}
