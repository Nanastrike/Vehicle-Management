<%@ page import="Fuel_dao.FuelConsumptionDAO" %>
<%@ page import="java.io.*" %>

<%--
 * File: Fuel_delete.jsp
 * Author: Xiaoxi Yang
 * Student ID: 041124876
 * Course: CST8288 
 * Section: 030/031
 * Date: 2025-04-05
 * 
 * Description:
 * This JSP script handles the deletion of a specific fuel consumption record 
 * based on the provided `id` parameter from the URL. It performs the following actions:
 * 
 * - Retrieves the `id` of the fuel consumption record from the request parameter.
 * - Uses `FuelConsumptionDAO` to execute the deletion in the database.
 * - Redirects to `Fuel_dashboard.jsp` upon successful deletion.
 * - Displays an error message if deletion fails or no ID is provided.
 *
 * This page is typically accessed through the "Delete" button on the fuel consumption dashboard.
--%>


<%
    String idStr = request.getParameter("id");
    if (idStr != null) {
        int id = Integer.parseInt(idStr);
        FuelConsumptionDAO dao = new FuelConsumptionDAO();
        boolean deleted = dao.deleteFuelConsumption(id);
        if (deleted) {
            response.sendRedirect("Fuel_dashboard.jsp");
        } else {
            out.println("Failed to delete the record.");
        }
    } else {
        out.println("No ID provided.");
    }
%>
