<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map, model.VehicleManagement.Vehicle" %>

<%
    Map<String, Integer> vehicleTypeCounts = (Map<String, Integer>) request.getAttribute("vehicleTypeCounts");
    Vehicle lastVehicle = (Vehicle) request.getAttribute("lastVehicle");
%>

<div class="vehicle-overview">
    <!-- Flex container for title and link -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <h2 style="margin: 0;">Vehicle Overview</h2>
        <a href="VehicleManagementServlet?action=list" class="view-link" style="text-decoration: none; font-weight: 600; color: #4682B4; font-size: 14px; transition: color 0.3s;">
            View Details
        </a>
    </div>

    <!-- Flex container for count and last vehicle details -->
    <div style="display: flex; justify-content: space-between; gap: 20px;">

        <!-- Vehicle Type Counts Section -->
        <div style="width: 50%;">
        <h3>Vehicle Type Summary</h3>
        <ul style="padding-left: 20px;">
            <% if (vehicleTypeCounts != null && !vehicleTypeCounts.isEmpty()) {
                for (Map.Entry<String, Integer> entry : vehicleTypeCounts.entrySet()) { %>
                    <li><strong><%= entry.getKey() %>:</strong> <%= entry.getValue() %></li>
                    <br> <!-- Add blank line after each item -->
            <%  }
            } else { %>
                <li>No vehicle data available.</li>
            <% } %>
        </ul>
    </div>



        <!-- Last Added Vehicle Section -->
        <div style="width: 50%;">
            <h3>Last Added Vehicle</h3>
            <% if (lastVehicle != null) { %>
                <p><strong>Vehicle Number:</strong> <%= lastVehicle.getVehicleNumber() %></p>
                <p><strong>Vehicle Type:</strong> <%= lastVehicle.getVehicleType().getTypeName() %></p>
                <p><strong>Fuel Type:</strong> <%= lastVehicle.getFuelType().getTypeName() %></p>
                <p><strong>Max Passengers:</strong> <%= lastVehicle.getMaxPassengers() %></p>
                <p><strong>Consumption Rate:</strong> <%= lastVehicle.getConsumptionRate() %> km/l</p>
                <p><strong>Last Maintenance Date:</strong> <%= lastVehicle.getLastMaintenanceDate() != null ? lastVehicle.getLastMaintenanceDate() : "N/A" %></p>
            <% } else { %>
                <p>No vehicles have been added yet.</p>
            <% } %>
        </div>
    </div>
</div>
