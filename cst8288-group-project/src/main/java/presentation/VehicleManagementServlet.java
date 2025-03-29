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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    // Update doGet method in VehicleManagementServlet.java
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            listVehicles(request, response);
        } else if (action.equals("dashboard")) {
            // Get vehicle type counts and last added vehicle
            Map<String, Integer> vehicleTypeCounts = getVehicleTypeCounts();
            Vehicle lastVehicle = getLastAddedVehicle();

            // Pass data to the dashboard
            request.setAttribute("vehicleTypeCounts", vehicleTypeCounts);
            request.setAttribute("lastVehicle", lastVehicle);

            // Forward to the dashboard
            request.getRequestDispatcher("DashboardPage.jsp").forward(request, response);
        } else {
            response.sendRedirect("DashboardPage.jsp"); // Fallback to dashboard
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
        throws IOException, ServletException {
    try {
        System.out.println("Received request to insert vehicle.");

        String vehicleNumber = request.getParameter("vehicleNumber");
        String vehicleTypeParam = request.getParameter("vehicleTypeID");
        String fuelTypeParam = request.getParameter("fuelTypeID");
        String consumptionRateParam = request.getParameter("consumptionRate");
        String maxPassengersParam = request.getParameter("maxPassengers");
        String routeIDParam = request.getParameter("routeID");
        String lastMaintenanceDateParam = request.getParameter("lastMaintenanceDate");

        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Vehicle Type ID: " + vehicleTypeParam);
        System.out.println("Fuel Type ID: " + fuelTypeParam);
        System.out.println("Consumption Rate: " + consumptionRateParam);
        System.out.println("Max Passengers: " + maxPassengersParam);
        System.out.println("Route ID: " + routeIDParam);
        System.out.println("Last Maintenance Date: " + lastMaintenanceDateParam);

        if (vehicleNumber == null || vehicleNumber.isEmpty()) {
            request.setAttribute("error", "Vehicle Number is required.");
            request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
            return;
        }

        // Continue only if vehicleNumber is valid
        int vehicleTypeID = Integer.parseInt(vehicleTypeParam);
        int fuelTypeID = Integer.parseInt(fuelTypeParam);
        float consumptionRate = Float.parseFloat(consumptionRateParam);
        int maxPassengers = Integer.parseInt(maxPassengersParam);
        int routeID = (routeIDParam != null && !routeIDParam.isEmpty()) ? Integer.parseInt(routeIDParam) : 0;
        Date lastMaintenanceDate = Date.valueOf(lastMaintenanceDateParam);

        VehicleType vehicleType = new VehicleType(vehicleTypeID, getVehicleTypeName(vehicleTypeID));
        FuelType fuelType = new FuelType(fuelTypeID, getFuelTypeName(fuelTypeID));

        Vehicle newVehicle = new Vehicle(0, vehicleNumber, vehicleType, fuelType, consumptionRate, maxPassengers, routeID, lastMaintenanceDate);

        // Check if vehicle number already exists
        if (vehicleDAO.isVehicleNumberExists(vehicleNumber)) {
            System.out.println("Vehicle number already exists.");
            request.setAttribute("error", "Vehicle number already exists. Please choose a different number.");
            request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
            return;
        }

        if (vehicleDAO.addVehicle(newVehicle)) {
            System.out.println("Vehicle added successfully!");
            response.sendRedirect("VehicleManagementServlet?action=list");
        } else {
            System.out.println("Failed to add vehicle.");
            request.setAttribute("error", "Failed to add vehicle. Please try again.");
            request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
        }
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Invalid input. Please try again.");
        request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
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

    private void updateVehicle(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
    try {
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
            response.sendRedirect("VehicleManagementServlet?action=list");
        } else {
            System.out.println("Failed to update vehicle.");
            response.sendRedirect("EditVehiclePage.jsp?vehicleID=" + vehicleID + "&error=Failed to update vehicle.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("EditVehiclePage.jsp?error=Invalid+input.+Please+try+again.");
    }
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
    
    // Get Vehicle Counts by Type
    private Map<String, Integer> getVehicleTypeCounts() {
        Map<String, Integer> vehicleTypeCounts = new HashMap<>();
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();

        for (Vehicle vehicle : vehicleList) {
            String typeName = vehicle.getVehicleType().getTypeName();
            vehicleTypeCounts.put(typeName, vehicleTypeCounts.getOrDefault(typeName, 0) + 1);
        }

        return vehicleTypeCounts;
    }
    
    // Get the Most Recently Added Vehicle
    private Vehicle getLastAddedVehicle() {
        List<Vehicle> vehicleList = vehicleDAO.getAllVehicles();
        if (!vehicleList.isEmpty()) {
            return vehicleList.get(vehicleList.size() - 1); // Return the last added vehicle
        }
        return null; // Return null if no vehicles exist
    }


}
