<%@ page import="model.MaintenanceTask.MaintenanceTask" %>

<%
    Integer highPriorityCount = (Integer) request.getAttribute("highPriorityCount");
    MaintenanceTask recentTask = (MaintenanceTask) request.getAttribute("recentTask");
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
            <% if (recentTask != null) { %>
                <p><strong>Vehicle ID:</strong> <%= recentTask.getVehicleId() %></p>
                <p><strong>Task Type:</strong> <%= recentTask.getTaskType() %></p>
                <p><strong>Priority:</strong> <%= recentTask.getPriority() %></p>
                <p><strong>Status:</strong> <%= recentTask.getStatus() %></p>
                <p><strong>Scheduled Date:</strong> <%= recentTask.getScheduledDate() %></p>
            <% } else { %>
                <p>No recent maintenance task found.</p>
            <% } %>
        </div>
    </div>
</div>
