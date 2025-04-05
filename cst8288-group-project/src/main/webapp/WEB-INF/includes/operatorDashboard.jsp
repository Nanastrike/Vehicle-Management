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
    <title>Operator Dashboard</title>
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
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            height: auto;
        }

        .card {
            background-color: #fff;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: left;
            transition: transform 0.3s, box-shadow 0.3s;
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

        .card h2 {
            margin: 0;
            font-size: 20px;
            color: #555;
        }

        .card p {
            color: #777;
            font-size: 14px;
            margin-top: 10px;
        }

        .view-link {
            font-size: 14px;
            text-decoration: none;
            color: #4682B4;
            font-weight: 600;
            transition: color 0.3s;
        }

        .view-link:hover {
            color: #0056b3;
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <!-- Main Container -->
    <div class="container">

        <!-- Operator Overview Cards -->
        <div class="card-container row-1">
            <div class="card">
                <jsp:include page="/WEB-INF/includes/vehicleOverviewCard.jsp" />
            </div>
            <jsp:include page="/WEB-INF/includes/gpsTrackingCard.jsp" />
        </div>

    </div>

</body>
</html>

