package Fuel_controller;

import Fuel_dao.FuelConsumptionDAO;
import Fuel_model.FuelConsumption;
import data.VehicleDAO;
import model.VehicleManagement.Vehicle;
import Fuel_service.FuelService;
import Fuel_observer.ConsumptionMonitor;
import Fuel_observer.AlertService;
import data.DatabaseConnection;
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

        // 获取 vehicleNumber 而不是 vehicleId
        String vehicleNumber = request.getParameter("vehicleNumber");
        double distance = parseDouble(request.getParameter("distance"), 100.0);

        // 实例化 DAO
        VehicleDAO vehicleDAO = new VehicleDAO();

        // 根据 vehicleNumber 查询车辆对象
        Vehicle vehicle = vehicleDAO.getVehicleByVehicleNumber(vehicleNumber);

        if (vehicle == null) {
            request.setAttribute("error", "Vehicle not found in database.");
            request.getRequestDispatcher("/Fuel_dashboard.jsp").forward(request, response);
            return;
        }

        // ✅ 计算消耗
        double result = fuelService.calculateFuel(vehicle, distance);
        
        FuelConsumption record = new FuelConsumption();
        record.setVehicleId(vehicle.getVehicleID());
        record.setFuelTypeId(vehicle.getFuelType().getFuelTypeID());
        record.setFuelUsed((float) result);
        record.setDistanceTraveled((float) distance);
        record.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

        FuelConsumptionDAO fuelDAO = new FuelConsumptionDAO();
        fuelDAO.insertFuelConsumption(record);

        // ✅ 设置用于 JSP 显示的参数
        request.setAttribute("calculatedConsumption", result);
        request.setAttribute("vehicleNumber", vehicleNumber);
        request.setAttribute("vehicleType", vehicle.getVehicleType().getTypeName());
        request.setAttribute("fuelType", vehicle.getFuelType().getTypeName());

        if (result > 50.0) {
            request.setAttribute("alertMessage", "ALERT: Consumption exceeded threshold!");
        }

        // 返回 dashboard 页面
        request.getRequestDispatcher("/Fuel_dashboard.jsp").forward(request, response);
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
