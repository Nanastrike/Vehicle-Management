<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.VehicleManagement.Vehicle" %>

<%
    List<Vehicle> vehicleList = (List<Vehicle>) request.getAttribute("vehicleList");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Vehicle Management</title>
    <!-- Google Font - Quicksand for consistency -->
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        /* Navbar Styles */
        .navbar {
            background-color: #fff;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            border-bottom: 1px solid #ddd;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .navbar a {
            color: #555;
            padding: 14px 20px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease-in-out;
        }
        .navbar a:hover {
            background-color: rgba(0, 0, 0, 0.05);
            border-radius: 5px;
            color: #333;
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
            text-align: left;
        }
        .add-btn {
            background-color: #4682B4;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: background-color 0.3s ease-in-out;
        }
        .add-btn:hover {
            background-color: #4169E1;
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
            background-color: #20c997;
            color: white;
        }
        .edit-btn:hover {
            background-color: #17a589;
        }
        .delete-btn {
            background-color: #dc3545;
            color: white;
        }
        .delete-btn:hover {
            background-color: #c82333;
        }
        .no-data {
            text-align: center;
            padding: 20px;
            color: #777;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <div class="navbar">
        <div>
            <a href="DashboardPage.jsp">Home</a>
            <a href="VehiclesPage.jsp">Vehicle Management</a>
            <a href="gps_tracking.jsp">GPS Tracking</a>
            <a href="fuel_monitor.jsp">Fuel/Energy Monitor</a>
            <a href="maintenance.jsp">Maintenance Alerts</a>
            <a href="reports.jsp">Reports</a>
        </div>
    </div>

    <!-- Main Container -->
    <div class="container">
        <h2>Vehicle Management</h2>

        <!-- Add Vehicle Button -->
        <div style="text-align: right; margin-bottom: 20px;">
            <a href="AddVehiclePage.jsp" class="add-btn">+ Add Vehicle</a>
        </div>

        <!-- Vehicle Table -->
        <table>
            <thead>
                <tr>
                    <th>Vehicle Number</th>
                    <th>Vehicle Type</th>
                    <th>Fuel Type</th>
                    <th>Max Passengers</th>
                    <th>Consumption Rate (km/l)</th>
                    <th>Last Maintenance</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                <% if (vehicleList != null && !vehicleList.isEmpty()) { 
                    for (Vehicle vehicle : vehicleList) { %>
                        <tr>
                            <td><%= vehicle.getVehicleNumber() %></td>
                            <td><%= vehicle.getVehicleType().getTypeName() %></td>
                            <td><%= vehicle.getFuelType().getTypeName() %></td>
                            <td><%= vehicle.getMaxPassengers() %></td>
                            <td><%= vehicle.getConsumptionRate() %></td>
                            <td><%= vehicle.getLastMaintenanceDate() != null ? vehicle.getLastMaintenanceDate() : "N/A" %></td>
                            <td>
                                <!-- Edit Button -->
                                <a href="EditVehiclePage.jsp?vehicleID=<%= vehicle.getVehicleID() %>" class="edit-btn">Edit</a>
                            </td>
                            <td>
                                <!-- Delete Button with Confirmation -->
                                <a href="VehicleManagementServlet?action=delete&vehicleID=<%= vehicle.getVehicleID() %>" 
                                   class="delete-btn"
                                   onclick="return confirm('Are you sure you want to delete this vehicle?');">Delete</a>
                            </td>
                        </tr>
                <%  } 
                } else { %>
                    <tr>
                        <td colspan="8" class="no-data">No vehicles found.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
