<%@ page import="model.User.User" %>
<%
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("LoginPage.jsp");
        return;
    }

    int userTypeId = user.getUserTypeId();
%>

<% if (userTypeId == 1) { %>
    <jsp:include page="/navbar.jsp" />
<% } else if (userTypeId == 2) { %>
    <jsp:include page="/operatorNavBar.jsp" />
<% } else { %>
    <div class="navbar"><p>Unknown Role</p></div>
<% } %>
