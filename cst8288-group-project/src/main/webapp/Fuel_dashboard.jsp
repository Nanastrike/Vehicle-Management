<%@page import="java.sql.Timestamp"%>
<%@page import="model.VehicleManagement.Vehicle"%>
<%@page import="java.util.List"%>
<%@page import="Fuel_model.FuelConsumption"%>
<%@page import="Fuel_dao.FuelConsumptionDAO"%>
<%@page import="data.VehicleDAO"%>
<%@ page import="data.DatabaseConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Fuel Consumption Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f8f9fa;
            color: #555;
        }
        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }
        .card {
            background-color: #fff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
            margin-bottom: 30px;
        }
        h2 {
            font-weight: 600;
            margin-bottom: 20px;
        }
        table th, table td {
            vertical-align: middle;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h2>Fuel Consumption Dashboard</h2>

        <form action="DashboardServlet" method="post" class="mb-4">
            <div class="row g-3 align-items-center">
                <div class="col-md-4">
                    <label for="vehicleNumber" class="form-label">Select Vehicle:</label>
                    <select name="vehicleNumber" class="form-select" required>
                        <option value="" disabled selected>-- Choose Vehicle --</option>
                        <%
                            VehicleDAO vdao = new VehicleDAO(DatabaseConnection.getInstance().getConnection());
                            List<Vehicle> vehicles = vdao.getAllVehicles();
                            for (Vehicle v : vehicles) {
                        %>
                        <option value="<%= v.getVehicleNumber() %>">
                            <%= v.getVehicleNumber() %> - <%= v.getVehicleType().getTypeName() %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="distance" class="form-label">Distance (km):</label>
                    <input type="number" name="distance" step="0.1" class="form-control" required/>
                </div>
                <div class="col-md-4 mt-4">
                    <button type="submit" class="btn btn-primary w-100">Calculate</button>
                </div>
            </div>
        </form>

        <%
            Double calculated = (Double) request.getAttribute("calculatedConsumption");
            String alert = (String) request.getAttribute("alertMessage");
            if (calculated != null) {
        %>
        <div class="alert alert-info">
            <strong>Result:</strong> Estimated fuel consumption: <%= String.format("%.2f", calculated) %> L
        </div>
        <% } if (alert != null) { %>
        <div class="alert alert-danger"><%= alert %></div>
        <% } %>

        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Vehicle ID</th>
                <th>Fuel Type</th>
                <th>Fuel Used (L)</th>
                <th>Distance (km)</th>
                <th>Timestamp</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                FuelConsumptionDAO dao = new FuelConsumptionDAO();
                List<FuelConsumption> list = dao.getAllFuelConsumption();

                for (FuelConsumption fc : list) {
                    float rate = fc.getFuelUsed() / fc.getDistanceTraveled() * 100;
                    String status;
                    String badgeClass;

                    if (rate < 10) {
                        status = "Normal";
                        badgeClass = "badge bg-success";
                    } else if (rate < 20) {
                        status = "Warning";
                        badgeClass = "badge bg-warning text-dark";
                    } else {
                        status = "Critical";
                        badgeClass = "badge bg-danger";
                    }
                    fc.setStatus(status);
            %>
            <tr>
                <td><%= fc.getConsumptionId() %></td>
                <td><%= fc.getVehicleId() %></td>
                <td><%= fc.getFuelTypeId() %></td>
                <td><%= fc.getFuelUsed() %></td>
                <td><%= fc.getDistanceTraveled() %></td>
                <td><%= fc.getTimestamp() %></td>
                <td><span class="<%= badgeClass %>"><%= fc.getStatus() %></span></td>
                <td>
                    <a href="Fuel_edit.jsp?id=<%= fc.getConsumptionId() %>" class="btn btn-sm btn-warning">Edit</a>
                    <a href="Fuel_delete.jsp?id=<%= fc.getConsumptionId() %>" class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure to delete this record?')">Delete</a>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
