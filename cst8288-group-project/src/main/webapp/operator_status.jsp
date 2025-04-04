<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.VehicleManagement.Vehicle" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="operatorNavBar.jsp" %>

<html>
<head>
    <title>Operator Dashboard</title>
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f5f5f5;
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
    </style>
</head>
<body>
<div class="container">
    <h2>Operator Dashboard</h2>

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
            <p class="distance">Current Distance: ${carDistance} km</p>

            <form action="takeBreak" method="post">
                <input type="hidden" name="vehicleId" value="${currentVehicle.vehicleID}" />
                <button type="submit">Take a Break</button>
            </form>

            <form action="stopDriving" method="post">
                <button type="submit">Stop Service</button>
            </form>
        </div>
    </c:if>
</div>
</body>
</html>
