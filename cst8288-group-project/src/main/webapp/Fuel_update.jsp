<%@ page import="java.sql.Timestamp, Fuel_model.FuelConsumption, Fuel_dao.FuelConsumptionDAO" %>

<%--
 * File: Fuel_update.jsp
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 *
 * Description:
 * This JSP page handles the POST submission from the fuel consumption edit form.
 *
 * Features:
 * - Retrieves form parameters for fuel consumption record update.
 * - Parses and validates input values including vehicle ID, fuel type ID, fuel used, and distance.
 * - Dynamically calculates consumption status ("Normal", "Warning", or "Critical") based on usage rate.
 * - Updates the record in the database using `FuelConsumptionDAO`.
 * - Redirects back to the dashboard upon success or displays an error on failure.
 *
 * This page supports the fuel tracking functionality within the PTFMS project.
--%>


<%
    request.setCharacterEncoding("UTF-8");

    String idParam = request.getParameter("consumptionId");
    String vehicleIdParam = request.getParameter("vehicleId");
    String fuelTypeIdParam = request.getParameter("fuelTypeId");
    String fuelUsedParam = request.getParameter("fuelUsed");
    String distanceParam = request.getParameter("distanceTraveled");

    if (idParam != null && vehicleIdParam != null && fuelTypeIdParam != null &&
        fuelUsedParam != null && distanceParam != null) {

        int id = Integer.parseInt(idParam);
        int vehicleId = Integer.parseInt(vehicleIdParam);
        int fuelTypeId = Integer.parseInt(fuelTypeIdParam);
        float fuelUsed = Float.parseFloat(fuelUsedParam);
        float distance = Float.parseFloat(distanceParam);

        String status;
        if (distance == 0) {
            status = "Normal"; 
        } else {
            float rate = fuelUsed / distance * 100;
            if (rate < 10) {
                status = "Normal";
            } else if (rate < 20) {
                status = "Warning";
            } else {
                status = "Critical";
            }
        }
          
        FuelConsumption fuel = new FuelConsumption();
        fuel.setConsumptionId(id);
        fuel.setVehicleId(vehicleId);
        fuel.setFuelTypeId(fuelTypeId);
        fuel.setFuelUsed(fuelUsed);
        fuel.setDistanceTraveled(distance);
        fuel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        fuel.setStatus(status); 

        FuelConsumptionDAO dao = new FuelConsumptionDAO();
        boolean updated = dao.updateFuelConsumption(fuel);

        if (updated) {
            response.sendRedirect("Fuel_dashboard.jsp");
        } else {
            out.println("<h3>Failed to update record.</h3>");
        }

    } else {
        out.println("<h3>Missing parameters. Cannot update.</h3>");
    }
%>
