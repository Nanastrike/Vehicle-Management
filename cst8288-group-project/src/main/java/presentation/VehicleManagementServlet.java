package presentation;

import data.VehicleDAO;
import model.VehicleManagement.*;
import data.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class VehicleManagementServlet extends HttpServlet {

    private Connection conn;
    private VehicleDAO vehicleDAO;

    @Override
    public void init() throws ServletException {
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            vehicleDAO = new VehicleDAO(conn);
        } catch (Exception e) {
            throw new ServletException("Error initializing VehicleManagementServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            listVehicles(request, response);
        } else if (action.equals("edit")) {
            showEditForm(request, response);
        } else if (action.equals("delete")) {
            deleteVehicle(request, response);
        } else if (action.equals("add")) {
            showAddForm(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("insert")) {
            insertVehicle(request, response);
        } else if (action.equals("update")) {
            updateVehicle(request, response);
        }
    }

    // List All Vehicles
    private void listVehicles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        System.out.println("Number of vehicles retrieved: " + vehicleList.size());
        request.setAttribute("vehicleList", vehicleList);
        request.getRequestDispatcher("VehiclesPage.jsp").forward(request, response);
    }

    // Show Add Vehicle Form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
    }

    // Insert New Vehicle
    private void insertVehicle(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            int vehicleTypeID = Integer.parseInt(request.getParameter("vehicleTypeID"));
            int fuelTypeID = Integer.parseInt(request.getParameter("fuelTypeID"));
            float consumptionRate = Float.parseFloat(request.getParameter("consumptionRate"));
            int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));

            String routeIDParam = request.getParameter("routeID");
            int routeID = (routeIDParam != null && !routeIDParam.isEmpty()) ? Integer.parseInt(routeIDParam) : 0;

            Date lastMaintenanceDate = Date.valueOf(request.getParameter("lastMaintenanceDate"));

            VehicleType vehicleType = new VehicleType(vehicleTypeID, getVehicleTypeName(vehicleTypeID));
            FuelType fuelType = new FuelType(fuelTypeID, getFuelTypeName(fuelTypeID));

            Vehicle newVehicle = new Vehicle(0, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);

            if (vehicleDAO.addVehicle(newVehicle)) {
                System.out.println("Vehicle added successfully!");
            } else {
                System.out.println("Failed to add vehicle.");
            }
            response.sendRedirect("VehicleManagementServlet?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AddVehiclePage.jsp?error=Invalid+input.+Please+try+again.");
        }
    }

    // Show Edit Form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
        Vehicle existingVehicle = vehicleDAO.getVehicleByID(vehicleID);
        request.setAttribute("vehicle", existingVehicle);
        request.getRequestDispatcher("EditVehiclePage.jsp").forward(request, response);
    }

    // Update Vehicle
    private void updateVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
        String vehicleNumber = request.getParameter("vehicleNumber");
        int vehicleTypeID = Integer.parseInt(request.getParameter("vehicleTypeID"));
        int fuelTypeID = Integer.parseInt(request.getParameter("fuelTypeID"));
        float consumptionRate = Float.parseFloat(request.getParameter("consumptionRate"));
        int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
        int routeID = Integer.parseInt(request.getParameter("routeID"));
        Date lastMaintenanceDate = Date.valueOf(request.getParameter("lastMaintenanceDate"));

        VehicleType vehicleType = new VehicleType(vehicleTypeID, getVehicleTypeName(vehicleTypeID));
        FuelType fuelType = new FuelType(fuelTypeID, getFuelTypeName(fuelTypeID));

        Vehicle updatedVehicle = new Vehicle(vehicleID, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);

        if (vehicleDAO.updateVehicle(updatedVehicle)) {
            System.out.println("Vehicle updated successfully!");
        } else {
            System.out.println("Failed to update vehicle.");
        }
        response.sendRedirect("VehicleManagementServlet?action=list");
    }

    // Delete Vehicle
    private void deleteVehicle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
        if (vehicleDAO.deleteVehicle(vehicleID)) {
            System.out.println("Vehicle deleted successfully.");
        } else {
            System.out.println("Failed to delete vehicle.");
        }
        response.sendRedirect("VehicleManagementServlet?action=list");
    }

    // Helper: Get Vehicle Type Name by ID
    private String getVehicleTypeName(int vehicleTypeID) {
        switch (vehicleTypeID) {
            case 1: return "Diesel Bus";
            case 2: return "Electric Light Rail";
            case 3: return "Diesel-Electric Train";
            default: return "Unknown";
        }
    }

    // Helper: Get Fuel Type Name by ID
    private String getFuelTypeName(int fuelTypeID) {
        switch (fuelTypeID) {
            case 1: return "Diesel";
            case 2: return "CNG";
            case 3: return "Electric";
            default: return "Unknown";
        }
    }
}
