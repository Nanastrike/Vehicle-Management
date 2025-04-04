<%@ page import="java.util.*, java.sql.*, Fuel_model.FuelConsumption, Fuel_dao.FuelConsumptionDAO" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    FuelConsumptionDAO dao = new FuelConsumptionDAO();
    FuelConsumption record = dao.getFuelConsumptionById(id);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Fuel Consumption</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="container mt-4">
    <h2>Edit Fuel Consumption</h2>
    <form action="Fuel_update.jsp" method="post">
        <input type="hidden" name="consumptionId" value="<%= record.getConsumptionId() %>" />

        <div class="mb-3">
            <label for="vehicleId" class="form-label">Vehicle ID:</label>
            <input type="number" name="vehicleId" class="form-control" value="<%= record.getVehicleId() %>" required />
        </div>

        <div class="mb-3">
            <label for="fuelTypeId" class="form-label">Fuel Type ID:</label>
            <input type="number" name="fuelTypeId" class="form-control" value="<%= record.getFuelTypeId() %>" required />
        </div>

        <div class="mb-3">
            <label for="fuelUsed" class="form-label">Fuel Used (L):</label>
            <input type="text" name="fuelUsed" class="form-control" value="<%= record.getFuelUsed() %>" required />
        </div>

        <div class="mb-3">
            <label for="distanceTraveled" class="form-label">Distance Traveled (km):</label>
            <input type="text" name="distanceTraveled" class="form-control" value="<%= record.getDistanceTraveled() %>" required />
        </div>

        <button type="submit" class="btn btn-primary">Update</button>
        <a href="Fuel_dashboard.jsp" class="btn btn-secondary">Cancel</a>
    </form>
</body>
</html>
