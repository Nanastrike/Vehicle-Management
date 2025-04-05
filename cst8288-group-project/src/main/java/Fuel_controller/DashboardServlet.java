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

/**
 * File: DashboardServlet.java
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This servlet handles POST requests from the Fuel Consumption Dashboard form.
 * It processes vehicle selection and distance input, calculates fuel or energy usage 
 * using the Strategy and Observer patterns, stores the result in the database, 
 * and forwards the output back to the dashboard for display.
 *
 * Key Responsibilities:
 * - Retrieve selected vehicle and input distance from the form.
 * - Use FuelService with appropriate strategy based on vehicle type.
 * - Calculate consumption and determine fuel status (Normal, Warning, Critical).
 * - Store result in the Fuel_Consumption table.
 * - Notify AlertService if consumption exceeds threshold.
 * - Forward results (including unit and alert) to Fuel_dashboard.jsp.
 */

@WebServlet(name = "DashboardServlet", urlPatterns = {"/DashboardServlet"})
public class DashboardServlet extends HttpServlet {

    private static ConsumptionMonitor monitor;
    private static AlertService alertService;
    private static FuelService fuelService;
    
    /**
    * Initializes the servlet, sets up the observer relationship between
    * the fuel consumption monitor and alert service, and instantiates the fuel service.
    *
    * @throws ServletException if initialization fails
    */
    @Override
    public void init() throws ServletException {
        super.init();
        monitor = new ConsumptionMonitor(50.0); // Default alert threshold
        alertService = new AlertService("Transit Manager");
        monitor.attach(alertService);

        fuelService = new FuelService(monitor);
    }
    
    /**
    * Handles the POST request from the fuel dashboard form.
    * Retrieves vehicle info and distance, performs calculation,
    * stores results, and forwards data to the JSP for display.
    *
    * @param request  the HttpServletRequest containing form parameters
    * @param response the HttpServletResponse to forward the result
    * @throws ServletException in case of servlet issues
    * @throws IOException      in case of input/output issues
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        
         // Determine status based on fuel usage rate
        String status;
        if (distance == 0) {
            status = "Normal"; 
        } else {
            float rate = (float) result / (float) distance * 100;
            if (rate < 10) {
                status = "Normal";
            } else if (rate < 20) {
                status = "Warning";
            } else {
                status = "Critical";
            }
        }
        record.setStatus(status);
        
        FuelConsumptionDAO fuelDAO = new FuelConsumptionDAO();
        fuelDAO.insertFuelConsumption(record);

        // Determine unit of measurement based on vehicle type
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

       
        // Set attributes for display
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

    /**
     * Utility method to parse a double from a string, falling back to default on failure.
     *
     * @param param         the input string to parse
     * @param defaultValue  the fallback value in case of parsing error
     * @return the parsed double or the default value
     */
    private double parseDouble(String param, double defaultValue) {
        if (param == null || param.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
