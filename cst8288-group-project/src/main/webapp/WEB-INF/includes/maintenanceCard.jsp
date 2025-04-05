<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MaintenanceTask.MaintenanceTask" %>


<%
    Integer highPriorityCount = (Integer) request.getAttribute("highPriorityCount");
    MaintenanceTask mostRecentTask = (MaintenanceTask) request.getAttribute("mostRecentTask");
%>

<div class="card">
    <!-- Title and View Link -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <h2 style="margin: 0;">Maintenance Overview</h2>
        <a href="MaintenanceServlet" class="view-link" style="text-decoration: none; font-weight: 600; color: #4682B4; font-size: 14px; transition: color 0.3s;">
            View Details
        </a>
    </div>

    <!-- Info Layout -->
    <div style="display: flex; justify-content: space-between; gap: 20px;">
        <!-- High Priority Count -->
        <div style="width: 50%;">
            <h3>High Priority Tasks</h3>
            <p style="font-size: 24px; font-weight: bold; color: #d9534f;">
                <%= highPriorityCount != null ? highPriorityCount : 0 %>
            </p>
        </div>

        <!-- Most Recent Task -->
        <div style="width: 50%;">
            <h3>Most Recent Task</h3>
            <% if (mostRecentTask != null) { %>
                <p><strong>Vehicle ID:</strong> <%= mostRecentTask.getVehicleId() %></p>
                <p><strong>Task Type:</strong> <%= mostRecentTask.getTaskType() %></p>
                <p><strong>Priority:</strong> <%= mostRecentTask.getPriority() %></p>
                <p><strong>Status:</strong> <%= mostRecentTask.getStatus() %></p>
                <p><strong>Scheduled Date:</strong> <%= mostRecentTask.getScheduledDate() %></p>
            <% } else { %>
                <p>No recent maintenance task found.</p>
            <% } %>
        </div>
    </div>
</div>
