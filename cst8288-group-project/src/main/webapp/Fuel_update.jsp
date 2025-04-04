<%@ page import="java.sql.Timestamp, model.FuelConsumption, dao.FuelConsumptionDAO" %>
<%
    int id = Integer.parseInt(request.getParameter("consumptionId"));
    int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
    int fuelTypeId = Integer.parseInt(request.getParameter("fuelTypeId"));
    float fuelUsed = Float.parseFloat(request.getParameter("fuelUsed"));
    float distance = Float.parseFloat(request.getParameter("distanceTraveled"));
    
    FuelConsumption record = new FuelConsumption();
    record.setConsumptionId(id);
    record.setVehicleId(vehicleId);
    record.setFuelTypeId(fuelTypeId);
    record.setFuelUsed(fuelUsed);
    record.setDistanceTraveled(distance);
    record.setTimestamp(new Timestamp(System.currentTimeMillis()));  // ???????

    FuelConsumptionDAO dao = new FuelConsumptionDAO();
    dao.updateFuelConsumption(record);

    response.sendRedirect("fuel-dashboard.jsp");
%>
