<%@ page import="java.util.List" %>
<%@ page import="model.FuelConsumption, model.Vehicle" %>
<%@ page import="dao.FuelConsumptionDAO, dao.VehicleDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Fuel Consumption Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="container mt-4">
    <h2>Fuel Consumption Dashboard</h2>

    <form action="DashboardServlet" method="post" class="mb-4">
        <div class="row g-3 align-items-center">
            <div class="col-md-4">
                <label for="vehicleId" class="form-label">Select Vehicle:</label>
                <select name="vehicleId" class="form-select" required>
                    <option value="" disabled selected>-- Choose Vehicle --</option>
                    <%
                        VehicleDAO vdao = new VehicleDAO();
                        List<Vehicle> vehicles = vdao.getAllVehicles();
                        for (Vehicle v : vehicles) {
                    %>
                        <option value="<%= v.getVehicleId() %>">
                            <%= v.getVehicleId() %> - <%= v.getVehicleType() %>
                        </option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="col-md-4">
                <label for="distance" class="form-label">Distance (km):</label>
                <input type="number" name="distance" step="0.1" class="form-control" required />
            </div>
            <div class="col-md-4 mt-4">
                <button type="submit" class="btn btn-primary">Calculate</button>
            </div>
        </div>
    </form>

    <!-- ✅ 显示计算结果 -->
    <%
        Double calculated = (Double) request.getAttribute("calculatedConsumption");
        String alert = (String) request.getAttribute("alertMessage");
        if (calculated != null) {
    %>
        <div class="alert alert-info">
            <strong>Result:</strong> Estimated fuel consumption: <%= String.format("%.2f", calculated) %> L
        </div>
    <%
        }
        if (alert != null) {
    %>
        <div class="alert alert-danger"><%= alert %></div>
    <%
        }
    %>

    <!-- 📊 表格：显示所有 fuel consumption 记录 -->
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
                float rate = fc.getFuelUsed() / fc.getDistanceTraveled();
                String status;
                String badgeClass;

                if (rate < 0.1) {
                    status = "Normal";
                    badgeClass = "badge bg-success";
                } else if (rate < 0.25) {
                    status = "Warning";
                    badgeClass = "badge bg-warning text-dark";
                } else {
                    status = "Critical";
                    badgeClass = "badge bg-danger";
                }
        %>
            <tr>
                <td><%= fc.getConsumptionId() %></td>
                <td><%= fc.getVehicleId() %></td>
                <td><%= fc.getFuelTypeId() %></td>
                <td><%= fc.getFuelUsed() %></td>
                <td><%= fc.getDistanceTraveled() %></td>
                <td><%= fc.getTimestamp() %></td>
                <td><span class="<%= badgeClass %>"><%= status %></span></td>
                <td>
                    <a href="edit-fuel.jsp?id=<%= fc.getConsumptionId() %>" class="btn btn-sm btn-warning">Edit</a>
                    <a href="delete-fuel.jsp?id=<%= fc.getConsumptionId() %>" class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure to delete this record?')">Delete</a>
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
</body>
</html>
