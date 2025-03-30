<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.VehicleManagement.Vehicle, model.VehicleManagement.VehicleType, model.VehicleManagement.FuelType" %>

<%
    Vehicle vehicle = (Vehicle) request.getAttribute("vehicle");
    if (vehicle == null) {
        response.sendRedirect("VehicleManagementServlet?action=list");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Vehicle</title>
    <!-- Google Font - Quicksand for consistency -->
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
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
            max-width: 600px; /* Consistent width with AddVehiclePage */
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #555;
            margin-bottom: 20px;
            font-size: larger;
        }
        label {
            font-weight: 600;
            color: #555;
            display: block;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 12px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
            box-sizing: border-box; /* Ensures padding and border are included */
        }
        /* Limit select width to 80% to make them shorter */
        select {
            width: 80%;
        }
        input:focus, select:focus {
            border-color: #4682B4;
            outline: none;
        }
        .btn-container {
            text-align: center;
            margin-top: 20px;
        }
        .btn {
            background-color: #4682B4;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            transition: background-color 0.3s;
        }
        .btn:hover {
            background-color: #4169E1;
        }
        .back-btn {
            background-color: #6c757d;
            text-decoration: none;
            color: white;
            padding: 12px 20px;
            border-radius: 8px;
            margin-top: 20px;
            font-size: 14px;
        }
        .back-btn:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

    <!-- Main Container -->
    <div class="container">
        <h2>Edit Vehicle</h2>

        <!-- Vehicle Form -->
        <form action="VehicleManagementServlet" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="vehicleID" value="<%= vehicle.getVehicleID() %>">

            <label for="vehicleNumber">Vehicle Number:</label>
            <input type="text" name="vehicleNumber" id="vehicleNumber" value="<%= vehicle.getVehicleNumber() %>" required>

            <label for="vehicleTypeID">Vehicle Type:</label>
            <select name="vehicleTypeID" id="vehicleTypeID" required>
                <option value="1" <%= vehicle.getVehicleType().getVehicleTypeID() == 1 ? "selected" : "" %>>Diesel Bus</option>
                <option value="2" <%= vehicle.getVehicleType().getVehicleTypeID() == 2 ? "selected" : "" %>>Electric Light Rail</option>
                <option value="3" <%= vehicle.getVehicleType().getVehicleTypeID() == 3 ? "selected" : "" %>>Diesel-Electric Train</option>
            </select>

            <label for="fuelTypeID">Fuel Type:</label>
            <select name="fuelTypeID" id="fuelTypeID" required>
                <option value="1" <%= vehicle.getFuelType().getFuelTypeID() == 1 ? "selected" : "" %>>Diesel</option>
                <option value="2" <%= vehicle.getFuelType().getFuelTypeID() == 2 ? "selected" : "" %>>CNG</option>
                <option value="3" <%= vehicle.getFuelType().getFuelTypeID() == 3 ? "selected" : "" %>>Electric</option>
            </select>

            <label for="consumptionRate">Consumption Rate (km/l):</label>
            <input type="number" step="0.01" name="consumptionRate" id="consumptionRate" value="<%= vehicle.getConsumptionRate() %>" required>

            <label for="maxPassengers">Max Passengers:</label>
            <input type="number" name="maxPassengers" id="maxPassengers" value="<%= vehicle.getMaxPassengers() %>" required>

            <label for="routeID">Route ID (Optional):</label>
            <input type="number" name="routeID" id="routeID" value="<%= vehicle.getRouteID() %>">

            <label for="lastMaintenanceDate">Last Maintenance Date:</label>
            <input type="date" name="lastMaintenanceDate" id="lastMaintenanceDate" value="<%= vehicle.getLastMaintenanceDate() %>" required>

            <!-- Submit and Back Buttons -->
            <div class="btn-container">
                <button type="submit" class="btn">Update Vehicle</button>
                <a href="VehicleManagementServlet?action=list" class="back-btn">Cancel</a>
            </div>
        </form>
    </div>

</body>
</html>
