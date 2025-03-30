<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.VehicleManagement.Vehicle, model.User.User" %>

<%
    // Retrieve the logged-in user from the session
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("LoginPage.jsp");
        return;
    }
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

        /* Main Container and Content */
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
            font-size: 24px;
            margin-bottom: 20px;
        }

        /* Add Button */
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

        /* Table Styles */
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

        /* Edit and Delete Buttons */
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

        .no-data {
            text-align: center;
            padding: 20px;
            color: #777;
        }

        /* Centering Action Buttons */
        th.action-col, td.action-col {
            text-align: center;
            vertical-align: middle;
        }
    </style>
</head>
<body>

    <!-- Include Navbar -->
    <jsp:include page="navbar.jsp" />

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
                    <th class="action-col">Edit</th>
                    <th class="action-col">Delete</th>
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
                            <td class="action-col">
                                <!-- Edit Button -->
                                <a href="VehicleManagementServlet?action=update&vehicleID=<%= vehicle.getVehicleID() %>" class="edit-btn">Edit</a>
                            </td>
                            <td class="action-col">
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
