<%@ page import="java.util.*, java.sql.*, model.FuelConsumption, dao.FuelConsumptionDAO" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    FuelConsumptionDAO dao = new FuelConsumptionDAO();
    FuelConsumption record = dao.getFuelConsumptionById(id);
%>

<html>
<head>
    <title>Edit Fuel Consumption</title>
</head>
<body>
    <h2>Edit Fuel Consumption</h2>
    <form action="update-fuel.jsp" method="post">
        <input type="hidden" name="consumptionId" value="<%= record.getConsumptionId() %>" />

        Vehicle ID: <input type="text" name="vehicleId" value="<%= record.getVehicleId() %>" /><br/>
        Fuel Type ID: <input type="text" name="fuelTypeId" value="<%= record.getFuelTypeId() %>" /><br/>
        Fuel Used: <input type="text" name="fuelUsed" value="<%= record.getFuelUsed() %>" /><br/>
        Distance Traveled: <input type="text" name="distanceTraveled" value="<%= record.getDistanceTraveled() %>" /><br/>

        <input type="submit" value="Update" />
        <a href="fuel-dashboard.jsp">Cancel</a>
    </form>
</body>
</html>

