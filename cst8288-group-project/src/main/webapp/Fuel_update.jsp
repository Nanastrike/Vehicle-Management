<%@ page import="java.sql.Timestamp, Fuel_model.FuelConsumption, Fuel_dao.FuelConsumptionDAO" %>
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
