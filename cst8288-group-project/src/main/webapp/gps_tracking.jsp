<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="module.GPS_Tracking.TrackingDisplayDTO" %>
<%@ include file="navbar.jsp" %>

<html>
    <head>
        <title>Vehicle Tracking Logs</title>
        <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
        <style>
            body {
                font-family: 'Quicksand', sans-serif;
                background-color: #f8f9fa;
                margin: 0;
                padding: 0;
            }

            .container {
                width: 95%;
                max-width: 1200px;
                margin: 30px auto;
                background-color: #ffffff;
                padding: 10px 20px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            h2 {
                margin-bottom: 20px;
                color: #333;
                font-size: 24px;
                font-weight: bold;
            }

            .search-container {
                display: flex;
                justify-content: flex-end;
                gap: 10px;
                margin-bottom: 20px;
            }

            .search-container input[type="text"] {
                padding: 6px 10px;
                width: 200px;
                border: 1px solid #333;
                border-radius: 4px;
            }

            .search-container button {
                background-color: #4682B4;
                color: white;
                padding: 12px 20px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 16px;
                font-weight: 600;
                transition: background-color 0.3s;
            }

            .search-container button:hover {
                background-color: #4169E1;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                border: none;
            }

            table, th, td {
                border: none;
            }

            th, td {
                border-top: none;
                border-left: none;
                border-right: none;
                border-bottom: 1px solid #ccc;  /* 只保留底部分隔线 */
                padding: 10px;
                text-align: center;
            }

            th {
                background-color: #4682B4;
                color: white;
                font-weight: bold;
            }

            .text-muted {
                color: #888;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Vehicle Tracking Logs</h2>

            <form class="search-container" action="gpsLogs" method="get">
                <input type="text" name="vehicleNumber" placeholder="Vehicle Number" />
                <button type="submit">Search</button>
                <button type="submit" name="refresh" value="true">Refresh</button>
            </form>

            <table>
                <thead>
                    <tr>
                        <th>Vehicle Number</th>
                        <th>Route Number</th>
                        <th>Position</th>
                        <th>Destination</th>
                        <th>Arrived</th>
                        <th>Leaving Time</th>
                        <th>Arrive Time</th>
                        <th>Operator</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<TrackingDisplayDTO> logs = (List<TrackingDisplayDTO>) request.getAttribute("logs");
                        if (logs == null || logs.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="7" class="text-muted">No record found.</td>
                    </tr>
                    <%
                    } else {
                        for (TrackingDisplayDTO log : logs) {
                    %>
                    <tr>
                        <td><%= log.getVehicleNumber()%></td>
                        <td><%= log.getRouteID()%></td>
                        <td><%= log.getPosition()%> km</td>
                        <td><%= log.getDestination()%></td>
                        <td><%= log.getIs_arrived()%></td>
                        <td><%= log.getLeavingTime() != null ? log.getLeavingTime().toLocalTime() : ""%></td>
                        <td><%= log.getArriveTime() != null ? log.getArriveTime().toLocalTime() : ""%></td>
                        <td><%= log.getOperatorName() != null ? log.getOperatorName() : "N/A"%></td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>
