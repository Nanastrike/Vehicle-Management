package Fuel_controller;

import Fuel_dao.VehicleDAO;
import Fuel_model.Vehicle;
import Fuel_service.FuelService;
import Fuel_observer.ConsumptionMonitor;
import Fuel_observer.AlertService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/DashboardServlet"})
public class DashboardServlet extends HttpServlet {

    private static ConsumptionMonitor monitor;
    private static AlertService alertService;
    private static FuelService fuelService;

    @Override
    public void init() throws ServletException {
        super.init();
        monitor = new ConsumptionMonitor(50.0); // Example threshold
        alertService = new AlertService("Transit Manager");
        monitor.attach(alertService);

        fuelService = new FuelService(monitor);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get vehicleId from form
        String vehicleIdStr = request.getParameter("vehicleId");
        int vehicleId = Integer.parseInt(vehicleIdStr);

        // 2. Get distance from form
        double distance = parseDouble(request.getParameter("distance"), 100.0);

        // 3. Get Vehicle object from DB
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {
            request.setAttribute("error", "Vehicle not found in database.");
            RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
            rd.forward(request, response);
            return;
        }

        // 4. Calculate fuel consumption
        double result = fuelService.calculateFuel(vehicle, distance);

        // 5. Store result for UI
        request.setAttribute("calculatedConsumption", result);
        request.setAttribute("vehicleId", vehicleId);
        request.setAttribute("vehicleType", vehicle.getVehicleType());
        request.setAttribute("fuelType", vehicle.getFuelType());

        if (result > 50.0) {
            request.setAttribute("alertMessage", "ALERT: Consumption exceeded threshold!");
        }

        // 6. Forward to dashboard.jsp
        RequestDispatcher rd = request.getRequestDispatcher("/dashboard.jsp");
        rd.forward(request, response);
    }

    private double parseDouble(String param, double defaultValue) {
        if (param == null || param.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
