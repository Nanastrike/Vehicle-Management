<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' hh:mm a");
    String currentTime = sdf.format(new Date());
%>

<div class="card">
    <div class="card-header">
        <h1 style="align-self: center; font-size: 40px; font-weight: bold; color: #333;">Welcome</h1>
    </div>
    <p style="font-weight: 600;font-size: 30px; color: #666; margin-top: 15px;">
        <%= currentTime %>
    </p>
</div>

