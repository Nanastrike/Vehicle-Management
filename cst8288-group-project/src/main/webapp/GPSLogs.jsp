<%-- 
    Document   : GPSLogs
    Created on : Apr 3, 2025, 1:24:07 p.m.
    Author     : silve
--%>

<%@page import="java.util.List"%>
<%@page import="module.GPS_Tracking.TrackingDisplayDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Vehicle Tracking Logs</title></head>
<body>
<h2>Vehicle Tracking Logs</h2>
<table border="1">
    <tr>
        <th>Vehicle Number</th>
        <th>Route Number</th>
        <th>Position</th>
        <th>Destination</th>
        <th>Arrived</th>
        <th>Leaving Time</th>
        <th>Arrive Time</th>
    </tr>

    <%
        List<TrackingDisplayDTO> logs = (List<TrackingDisplayDTO>) request.getAttribute("logs");
        for (TrackingDisplayDTO log : logs) {
    %>
    <tr>
        <td><%= log.getVehicleNumber() %></td>
        <td><%= log.getRouteID() %></td>
        <td><%= log.getPosition() %> km</td>
        <td><%= log.getDestination() %></td>
        <td><%= log.getIs_arrived() %></td>
        <td><%= log.getLeavingTime() != null ? log.getLeavingTime() : "" %></td>
        <td><%= log.getArriveTime() != null ? log.getArriveTime() : "" %></td>
    </tr>
    <% } %>
</table>
</body>
</html>