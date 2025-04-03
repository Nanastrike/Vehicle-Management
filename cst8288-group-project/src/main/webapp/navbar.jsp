<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User.User" %>

<%
    // Get logged-in user from session
    User user = (User) session.getAttribute("loggedInUser");
%>

<!-- Navbar -->
<div class="navbar">
    <div class="nav-links">
        <a href="VehicleManagementServlet?action=dashboard">Home</a>
        <a href="VehicleManagementServlet?action=list">Vehicle Management</a>
        <a href="gpsLogs">GPS Tracking</a>
        <a href="fuel_monitor.jsp">Fuel/Energy Monitor</a>
        <a href="MaintenanceServlet">Maintenance Alerts</a>
        <a href="reports.jsp">Reports</a>
    </div>

    <!-- User Info and Logout Button (if user is logged in) -->
    <% if (user != null) { %>
        <div class="user-container">
            <span class="user-info">Welcome, <%= user.getName() %>!</span>
            <a href="LogoutServlet" class="logout-btn">Log Out</a>
        </div>
    <% } %>
</div>

<!-- Navbar Styles -->
<style>
    .navbar {
        background-color: #fff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 20px;
        height: 60px;
        width: 100%;
        border-bottom: 1px solid #ddd;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        box-sizing: border-box;
    }
    .nav-links {
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .navbar a {
        color: #555;
        padding: 10px 15px;
        text-decoration: none;
        font-weight: 600;
        transition: all 0.3s ease-in-out;
    }
    .navbar a:hover {
        background-color: rgba(0, 0, 0, 0.05);
        border-radius: 5px;
        color: #333;
    }
    .user-container {
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .user-info {
        color: #555;
        font-weight: bold;
        font-size: 14px;
    }
    .logout-btn {
        background-color: #ccc;
        color: white;
        padding: 8px 12px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 14px;
        text-decoration: none;
        transition: background-color 0.3s;
    }
    .logout-btn:hover {
        background-color: #5a6268;
    }
</style>
