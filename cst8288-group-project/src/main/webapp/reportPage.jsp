<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
<head>
    <title>System Report</title>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        /* Make navbar span full width and fixed at the top */
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
            padding-top: 70px; /* Make space for fixed navbar */
        }

        .container {
            background: #fff;
            max-width: 900px;
            margin: auto;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        h2 {
            color: #333;
            font-size: 24px;
            margin-bottom: 15px;
        }

        .message {
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
        }

        pre {
            background: #f1f1f1;
            padding: 15px;
            border-radius: 8px;
            overflow-x: auto;
            white-space: pre-wrap;
            font-size: 14px;
        }

        .btn {
            padding: 10px 18px;
            border: none;
            background-color: #4682B4;
            color: white;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            margin-right: 10px;
            transition: 0.3s;
        }

        .btn:hover {
            background-color: #4169E1;
        }
    </style>
</head>

<body>
<jsp:include page="/WEB-INF/includes/navbarDispatcher.jsp" />
<div class="container">
    <h2>System Report</h2>

    <!-- Display status message -->
    <%
        String success = (String) request.getAttribute("success");
        String error = (String) request.getAttribute("error");
    %>

    <% if (success != null) { %>
        <div class="message success"><%= success %></div>
    <% } else if (error != null) { %>
        <div class="message error"><%= error %></div>
    <% } %>

    <!-- Report Title -->
    <%
        String reportTitle = (String) request.getAttribute("reportTitle");
        String reportContent = (String) request.getAttribute("reportContent");
    %>

    <% if (reportTitle != null && reportContent != null) { %>
        <h3><%= reportTitle %></h3>
        <pre><%= reportContent %></pre>
        <% if (reportTitle != null && reportContent != null) { %>
            <form action="DownloadReportServlet" method="post" style="display:inline;">
                <input type="hidden" name="reportTitle" value="<%= reportTitle %>">
                <input type="hidden" name="reportContent" value="<%= reportContent.replace("\"", "&quot;") %>">
                <button type="submit" class="btn">Download Report</button>
            </form>
    <% } %>

    <% } else { %>
        <p>No report content to display.</p>
    <% } %>

    <div style="margin-top: 20px;">
        <form action="ReportServlet" method="post" style="display: inline;">
            <input type="hidden" name="type" value="maintenance">
            <button type="submit" class="btn">Generate Maintenance Report</button>
        </form>

        <form action="ReportServlet" method="post" style="display: inline;">
            <input type="hidden" name="type" value="fuel">
            <button type="submit" class="btn">Generate Fuel Efficiency Report</button>
        </form>

        <form action="ReportServlet" method="post" style="display: inline;">
            <input type="hidden" name="type" value="operator">
            <button type="submit" class="btn">Generate Operator Efficiency Report</button>
        </form>
    </div>
</div>

</body>
</html>
