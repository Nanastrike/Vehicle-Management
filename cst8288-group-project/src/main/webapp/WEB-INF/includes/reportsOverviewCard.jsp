<%@page import="java.util.Map"%>
<%
    Map<String, Integer> reportCounts = (Map<String, Integer>) request.getAttribute("reportTypeCounts");
%>

<div class="card">
    <!-- Title and View Link -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <h2 style="margin: 0;">Reports Overview</h2>
        <a href="reportPage.jsp" class="view-link" style="text-decoration: none; font-weight: 600; color: #4682B4; font-size: 14px; transition: color 0.3s;">
            View Details
        </a>
    </div>

    <!-- Report Counts Grid -->
    <div style="display: flex; justify-content: space-around; padding: 10px 0; gap: 20px;">
        <div style="text-align: center;">
            <h4 style="margin-bottom: 5px;">Maintenance</h4>
            <p style="font-size: 22px; font-weight: bold; color: #4682B4;">
                <%= reportCounts != null && reportCounts.containsKey("maintenance") ? reportCounts.get("maintenance") : 0 %>
            </p>
        </div>
        <div style="text-align: center;">
            <h4 style="margin-bottom: 5px;">Fuel</h4>
            <p style="font-size: 22px; font-weight: bold; color: #20B2AA;">
                <%= reportCounts != null && reportCounts.containsKey("fuel") ? reportCounts.get("fuel") : 0 %>
            </p>
        </div>
        <div style="text-align: center;">
            <h4 style="margin-bottom: 5px;">Operator</h4>
            <p style="font-size: 22px; font-weight: bold; color: #FF8C00;">
                <%= reportCounts != null && reportCounts.containsKey("operator") ? reportCounts.get("operator") : 0 %>
            </p>
        </div>
    </div>
</div>
