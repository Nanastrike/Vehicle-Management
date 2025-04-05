package Fuel_controller;

import Fuel_dao.FuelConsumptionDAO;
import Fuel_model.FuelConsumption;
import data.VehicleDAO;
import model.VehicleManagement.Vehicle;
import Fuel_service.FuelService;
import Fuel_observer.ConsumptionMonitor;
import Fuel_observer.AlertService;
import data.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/DashboardServlet"})
public class DashboardServlet extends HttpServlet {

    private static ConsumptionMonitor monitor;
    private static AlertService alertService;
    private static FuelService fuelService;

    @Override
    public void init() throws ServletException {
        super.init();
        monitor = new ConsumptionMonitor(50.0); // Default alert threshold
        alertService = new AlertService("Transit Manager");
        monitor.attach(alertService);

        fuelService = new FuelService(monitor);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 从表单获取 vehicleNumber 和 distance
        String vehicleNumber = request.getParameter("vehicleNumber");
        double distance = parseDouble(request.getParameter("distance"), 100.0);

        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.getVehicleByVehicleNumber(vehicleNumber);

        if (vehicle == null) {
            request.setAttribute("error", "Vehicle not found in database.");
            request.getRequestDispatcher("/Fuel_dashboard.jsp").forward(request, response);
            return;
        }

       
        double result = fuelService.calculateFuel(vehicle, distance);

      
        FuelConsumption record = new FuelConsumption();
        record.setVehicleId(vehicle.getVehicleID());
        record.setFuelTypeId(vehicle.getFuelType().getFuelTypeID());
        record.setFuelUsed((float) result);
        record.setDistanceTraveled((float) distance);
        record.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

        FuelConsumptionDAO fuelDAO = new FuelConsumptionDAO();
        fuelDAO.insertFuelConsumption(record);

    
        String unit;
        String vehicleTypeName = vehicle.getVehicleType().getTypeName();

        switch (vehicleTypeName) {
            case "Electric Light Rail":
                unit = "kWh";
                break;
            case "Diesel Electric Train":
            case "Diesel Bus":
                unit = "L";
                break;
            default:
                unit = "L";
        }

       
        request.setAttribute("calculatedConsumption", result);
        request.setAttribute("unit", unit);
        request.setAttribute("vehicleNumber", vehicleNumber);
        request.setAttribute("vehicleType", vehicleTypeName);
        request.setAttribute("fuelType", vehicle.getFuelType().getTypeName());

        if (result > 50.0) {
            request.setAttribute("alertMessage", "ALERT: Consumption exceeded threshold!");
        }


        RequestDispatcher dispatcher = request.getRequestDispatcher("/Fuel_dashboard.jsp");
        dispatcher.forward(request, response);
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
