<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Timestamp, java.util.List" %>
<%@ page import="model.VehicleManagement.Vehicle" %>
<%@ page import="Fuel_model.FuelConsumption" %>
<%@ page import="Fuel_dao.FuelConsumptionDAO" %>
<%@ page import="data.VehicleDAO" %>
<%@ page import="data.DatabaseConnection" %>

<%
    // 登录验证（如果需要）
    // User user = (User) session.getAttribute("loggedInUser");
    // if (user == null) {
    //     response.sendRedirect("LoginPage.jsp");
    //     return;
    // }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Fuel Consumption Dashboard</title>
    <!-- 字体 -->
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #555;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .form-section {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-section .form-group {
            flex: 1;
        }

        .form-section button {
            margin-top: 32px;
            height: 38px;
        }

        .alert {
            margin-top: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #4682B4;
            color: white;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .edit-btn, .delete-btn {
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
        }

        .edit-btn {
            background-color: #198754;
            color: white;
        }

        .edit-btn:hover {
            background-color: #157347;
        }

        .delete-btn {
            background-color: #dc3545;
            color: white;
        }

        .delete-btn:hover {
            background-color: #c82333;
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: bold;
        }

        .status-normal {
            background-color: #198754;
            color: white;
        }

        .status-warning {
            background-color: #ffc107;
            color: black;
        }

        .status-critical {
            background-color: #dc3545;
            color: white;
        }
    </style>
</head>
<body>

<jsp:include page="/navbar.jsp" />

<div class="container">
    <h2>Fuel Consumption Dashboard</h2>

    <form action="DashboardServlet" method="post" class="mb-4">
    <div style="display: flex; align-items: center; gap: 16px;">
        <div>
            <label for="vehicleNumber" class="form-label">Select Vehicle:</label><br/>
            <select name="vehicleNumber" class="form-select" required style="width: 200px;">
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

        <div>
            <label for="distance" class="form-label">Distance (km):</label><br/>
            <input type="number" name="distance" step="0.1" class="form-control" style="width: 200px;" required/>
        </div>

        <div style="margin-top: 24px;">
            <button type="submit" class="btn btn-primary">Calculate</button>
        </div>
    </div>
    </form>


    <!-- 结果提示 -->
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

    <!-- 数据表格 -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Vehicle ID</th>
            <th>Fuel Type</th>
            <th>Fuel Used (L)</th>
            <th>Distance (km)</th>
            <th>Timestamp</th>
            <th>Status</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>
        <%
            FuelConsumptionDAO dao = new FuelConsumptionDAO();
            List<FuelConsumption> list = dao.getAllFuelConsumption();

            for (FuelConsumption fc : list) {
                float rate = fc.getFuelUsed() / fc.getDistanceTraveled() * 100;
                String status;
                String statusClass;

                if (rate < 10) {
                    status = "Normal";
                    statusClass = "status-badge status-normal";
                } else if (rate < 20) {
                    status = "Warning";
                    statusClass = "status-badge status-warning";
                } else {
                    status = "Critical";
                    statusClass = "status-badge status-critical";
                }
        %>
            <tr>
                <td><%= fc.getConsumptionId() %></td>
                <td><%= fc.getVehicleId() %></td>
                <td><%= fc.getFuelTypeId() %></td>
                <td><%= fc.getFuelUsed() %></td>
                <td><%= fc.getDistanceTraveled() %></td>
                <td><%= fc.getTimestamp() %></td>
                <td><span class="<%= statusClass %>"><%= status %></span></td>
                <td class="action-col">
                    <a href="Fuel_edit.jsp?id=<%= fc.getConsumptionId() %>" class="edit-btn">Edit</a>
                </td>
                <td class="action-col">
                    <a href="Fuel_delete.jsp?id=<%= fc.getConsumptionId() %>" 
                       class="delete-btn"
                       onclick="return confirm('Are you sure to delete this record?');">Delete</a>
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
