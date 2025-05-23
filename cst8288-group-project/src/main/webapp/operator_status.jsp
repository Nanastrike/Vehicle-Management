<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.VehicleManagement.Vehicle" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="operatorNavBar.jsp" %>

<html>
<head>
    <title>Operator Dashboard</title>
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f5f5f5;
            
        }
        .navbar {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            background-color: #fff;
            border-bottom: 1px solid #ddd;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            z-index: 1000;
        }
        body {
            padding-top: 70px;
        }
        .container {
            max-width: 800px;
            margin: 50px auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form {
            text-align: center;
            margin-top: 20px;
        }
        select, button {
            padding: 10px;
            font-size: 16px;
            margin: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .status-box {
            margin-top: 30px;
            text-align: center;
        }
        .distance {
            font-size: 20px;
            color: #2e8b57;
        }
        .message {
            color: green;
            font-weight: bold;
            text-align: center;
            display: block;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Operator Dashboard</h2>

    <!-- Display message if vehicle just arrived at the destination -->
    <c:if test="${justArrived}">
        <p class="message">Vehicle has reached the destination. Please select a new vehicle to start.</p>
    </c:if>

    <!-- Show vehicle selection form if not currently driving -->
    <c:if test="${!isDriving}">
        <form action="startDriving" method="post">
            <label for="vehicle">Select Vehicle:</label>
            <select name="vehicleId" id="vehicle">
                <c:forEach var="v" items="${vehicleList}">
                    <option value="${v.vehicleID}">${v.vehicleNumber}</option>
                </c:forEach>
            </select>
            <button type="submit">Start Driving</button>
        </form>
    </c:if>

    <!-- Show driving status and distance tracking if already driving -->
    <c:if test="${isDriving}">
        <div class="status-box">
            <c:choose>
                <c:when test="${not empty currentVehicle}">
                    <p>You are currently driving vehicle <strong>${currentVehicle.vehicleNumber}</strong>.</p>
                </c:when>
                <c:otherwise>
                    <p style="color:red;">[Error] Vehicle data not found in session.</p>
                </c:otherwise>
            </c:choose>

            <p class="distance">
                Current Distance: <fmt:formatNumber value="${carDistance}" type="number" maxFractionDigits="2" /> km
            </p>

            <c:choose>
                <c:when test="${isPaused}">
                    <form action="resumeDriving" method="post">
                        <button type="submit">Resume Driving</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="pauseDriving" method="post">
                        <button type="submit">Take a Break</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</div>
</body>
</html>
