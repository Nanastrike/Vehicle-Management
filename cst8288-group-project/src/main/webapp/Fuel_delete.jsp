<%@ page import="dao.FuelConsumptionDAO" %>
<%@ page import="java.io.*" %>

<%
    String idStr = request.getParameter("id");
    if (idStr != null) {
        int id = Integer.parseInt(idStr);
        FuelConsumptionDAO dao = new FuelConsumptionDAO();
        boolean deleted = dao.deleteFuelConsumption(id);
        if (deleted) {
            response.sendRedirect("fuel-dashboard.jsp");
        } else {
            out.println("Failed to delete the record.");
        }
    } else {
        out.println("No ID provided.");
    }
%>
