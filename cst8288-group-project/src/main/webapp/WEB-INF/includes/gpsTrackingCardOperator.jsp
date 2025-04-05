<%@ page import="java.util.List" %>
<%@ page import="data.gps_tracking.VehicleActionDTO" %>

<%
    Integer runningVehicleCount = (Integer) request.getAttribute("runningVehicleCount");
    List<VehicleActionDTO> recentVehicles = (List<VehicleActionDTO>) request.getAttribute("recentVehicles");
%>

<div class="card">
    <!-- Flex container for title and link -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <h2 style="margin: 0;">GPS Tracking Overview</h2>
        <a href="${pageContext.request.contextPath}/operatorDashboard" class="view-link" style="text-decoration: none; font-weight: 600; color: #4682B4; font-size: 14px; transition: color 0.3s;">
            View Details
        </a>
    </div>

    <div style="display: flex; gap: 30px; flex-wrap: wrap;">
        <!-- Running Vehicles -->
        <div style="flex: 1; min-width: 250px;">
            <h4 style="margin-bottom: 10px;">Running Vehicles</h4>
            <p style="font-size: 28px; font-weight: bold; color: #28a745;">
                <%= runningVehicleCount != null ? runningVehicleCount : 0 %>
            </p>
        </div>

        <!-- Recent Vehicle Actions -->
        <div style="flex: 2; min-width: 300px;">
            <h4 style="margin-bottom: 10px;">Recent Vehicle Movements</h4>
            <% if (recentVehicles != null && !recentVehicles.isEmpty()) { %>
                <ul style="padding-left: 20px;">
                    <% for (VehicleActionDTO v : recentVehicles) { %>
                        <li style="margin-bottom: 8px;">
                            <strong>Vehicle ID:</strong> <%= v.getVehicleID() %> |
                            <strong>Operator:</strong> <%= v.getOperatorName() %> |
                            <strong>Departure:</strong> <%= v.getLeavingTime() %>
                        </li>
                    <% } %>
                </ul>
            <% } else { %>
                <p>No recent GPS tracking data found.</p>
            <% } %>
        </div>
    </div>
</div>


