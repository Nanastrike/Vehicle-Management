<%@page import="java.util.List"%>
<%@ page import="Fuel_model.FuelConsumption" %>
<%
    Integer criticalFuelCount = (Integer) request.getAttribute("criticalFuelCount");
    List<FuelConsumption> recentFuelList = (List<FuelConsumption>) request.getAttribute("recentFuelList");
%>

<div class="card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <h2 style="margin: 0;">Fuel/Energy Overview</h2>
        <a href="Fuel_dashboard.jsp" class="view-link"
           style="text-decoration: none; font-weight: 600; color: #4682B4; font-size: 14px;">
            View Details
        </a>
    </div>

    <div style="display: flex; justify-content: space-between; gap: 20px;">
        <!-- Critical Count -->
        <div style="width: 50%;">
            <h3>Critical Fuel Status</h3>
            <p style="font-size: 24px; font-weight: bold; color: #dc3545;">
                <%= criticalFuelCount != null ? criticalFuelCount : 0 %>
            </p>
        </div>

        <!-- Recent Records -->
        <div style="width: 50%;">
            <h3>Recent Fuel Records</h3>
            <% if (recentFuelList != null && !recentFuelList.isEmpty()) { %>
                <ul style="padding-left: 16px;">
                    <% for (FuelConsumption fc : recentFuelList) { %>
                        <li>
                            <strong>VehicleID:</strong> <%= fc.getVehicleId() %> |
                            <strong>Fuel Used:</strong> <%= fc.getFuelUsed() %> L |
                            <strong>Status:</strong> <%= fc.getStatus() %>
                        </li>
                    <% } %>
                </ul>
            <% } else { %>
                <p>No recent fuel data.</p>
            <% } %>
        </div>
    </div>
</div>
