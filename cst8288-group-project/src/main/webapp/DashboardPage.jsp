<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>

<%
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("LoginPage.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <!-- Google Font - Quicksand for a softer, rounded feel -->
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #555;
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

        /* Flex container for user info and logout button */
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
            background-color: #6c757d; /* Grey background */
            color: white !important; /* White text */
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

        /* Container and Search Bar */
        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }
        .search-container {
            margin: 20px auto;
            text-align: center;
        }
        .search-bar {
            width: 80%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 25px;
            font-size: 16px;
            outline: none;
            transition: border 0.3s;
        }
        .search-bar:focus {
            border-color: #777;
        }

        /* Dashboard Card Layout */
        .card-container {
            display: grid;
            gap: 20px;
            margin-bottom: 20px;
        }

        /* Row Heights */
        .row-1 {
            display: grid;
            grid-template-columns: 70% 30%;
            gap: 20px;
            height: 200px;
        }
        .row-2 {
            display: grid;
            grid-template-columns: 60% 40%;
            gap: 20px;
            height: 400px;
        }
        .row-3 {
            display: grid;
            grid-template-columns: 45% 55%;
            gap: 20px;
            height: 300px;
        }

        /* Card Styling */
        .card {
            background-color: #fff;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: left;
            transition: transform 0.3s, box-shadow 0.3s, background-color 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            background-color: rgba(0, 0, 0, 0.03);
        }
        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .card h3 {
            margin-top: 0;
            font-size: 20px;
            color: #555;
        }
        .card p {
            color: #777;
            font-size: 14px;
        }

        /* View Details Link */
        .view-link {
            font-size: 14px;
            text-decoration: none;
            color: #007bff; /* Link color */
            font-weight: 600;
            transition: color 0.3s;
        }
        .view-link:hover {
            color: #0056b3; /* Darker blue on hover */
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <div class="navbar">
        <!-- Navigation Links -->
        <div>
            <a href="dashboard.jsp">Home</a>
            <a href="vehicles.jsp">Vehicle Management</a>
            <a href="gps_tracking.jsp">GPS Tracking</a>
            <a href="fuel_monitor.jsp">Fuel/Energy Monitor</a>
            <a href="maintenance.jsp">Maintenance Alerts</a>
            <a href="reports.jsp">Reports</a>
        </div>

        <!-- User Info & Log Out Button -->
        <div class="user-container">
            <span class="user-info">Welcome, <%= user.getName() %>!</span>
            <a href="LogoutServlet" class="logout-btn">Log Out</a>
        </div>
    </div>

    <!-- Main Container -->
    <div class="container">

        <!-- Search Bar -->
        <div class="search-container">
            <input type="text" class="search-bar" placeholder="Search...">
        </div>

        <!-- Row 1: Whole Overview and Reports Overview -->
        <div class="card-container row-1">
            <div class="card">
                <div class="card-header">
                    <h3><strong>Whole Overview</strong></h3>
                </div>
                <p>Get a complete overview of system activities.</p>
            </div>
            <div class="card">
                <div class="card-header">
                    <h3><strong>Reports Overview</strong></h3>
                    <a href="reports.jsp" class="view-link">View Details</a>
                </div>
                <p>Generate and analyze system reports.</p>
            </div>
        </div>

        <!-- Row 2: Vehicle Management and GPS Tracking Overview -->
        <div class="card-container row-2">
            <div class="card">
                <div class="card-header">
                    <h3><strong>Vehicle Management Overview</strong></h3>
                    <a href="vehicles.jsp" class="view-link">View Details</a>
                </div>
                <p>Manage and view all vehicles.</p>
            </div>
            <div class="card">
                <div class="card-header">
                    <h3><strong>GPS Tracking Overview</strong></h3>
                    <a href="gps_tracking.jsp" class="view-link">View Details</a>
                </div>
                <p>Track vehicle locations in real-time.</p>
            </div>
        </div>

        <!-- Row 3: Maintenance Overview and Fuel/Energy Overview -->
        <div class="card-container row-3">
            <div class="card">
                <div class="card-header">
                    <h3><strong>Maintenance Overview</strong></h3>
                    <a href="maintenance.jsp" class="view-link">View Details</a>
                </div>
                <p>Track upcoming maintenance alerts.</p>
            </div>
            <div class="card">
                <div class="card-header">
                    <h3><strong>Fuel/Energy Overview</strong></h3>
                    <a href="fuel_monitor.jsp" class="view-link">View Details</a>
                </div>
                <p>Monitor fuel and energy usage.</p>
            </div>
        </div>

    </div>
</body>
</html>
