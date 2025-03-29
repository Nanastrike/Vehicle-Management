<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User.User" %>

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
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #555;
        }
        .container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }
        .card-container {
            display: grid;
            gap: 20px;
            margin-bottom: 20px;
        }
        .row-1 {
            display: grid;
            grid-template-columns: 70% 30%;
            gap: 20px;
            height: auto;
        }
        .row-2 {
            display: grid;
            grid-template-columns: 60% 40%;
            gap: 20px;
            height: auto;
        }
        .row-3 {
            display: grid;
            grid-template-columns: 45% 55%;
            gap: 20px;
            height: 300px;
        }
        .card {
            background-color: #fff;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: left;
        }
    </style>
</head>
<body>

    <!-- Include Navbar -->
    <jsp:include page="navbar.jsp" />

    <!-- Main Container -->
    <div class="container">

        <!-- Row 1: Whole Overview and Reports Overview -->
        <div class="card-container row-1">
            <jsp:include page="/WEB-INF/includes/wholeOverviewCard.jsp" />
            <jsp:include page="/WEB-INF/includes/reportsOverviewCard.jsp" />
        </div>

        <!-- Row 2: Vehicle Management and GPS Tracking Overview -->
        <div class="card-container row-2">
            <div class="card">
                <!-- Include Vehicle Overview (Counts + Last Vehicle) -->
                <jsp:include page="/WEB-INF/includes/vehicleOverviewCard.jsp" />
            </div>
            
            <!-- Include GPS Tracking Card -->
            <jsp:include page="/WEB-INF/includes/gpsTrackingCard.jsp" />
        </div>

        <!-- Row 3: Maintenance and Fuel/Energy Overview -->
        <div class="card-container row-3">
            <!-- Include Maintenance Card -->
            <jsp:include page="/WEB-INF/includes/maintenanceCard.jsp" />
            <!-- Include Fuel/Energy Card -->
            <jsp:include page="/WEB-INF/includes/fuelEnergyCard.jsp" />
        </div>

    </div>
</body>
</html>
