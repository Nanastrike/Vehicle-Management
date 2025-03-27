<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>
    <h2>Register</h2>

    <%-- Display error message if registration fails --%>
    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <%-- Display success message if registration succeeds --%>
    <% if (request.getAttribute("success") != null) { %>
        <p class="success"><%= request.getAttribute("success") %></p>
    <% } %>

    <form method="post" action="RegisterServlet">
        <label for="name">Name:</label><br>
        <input type="text" name="name" required><br>

        <label for="email">Email:</label><br>
        <input type="email" name="email" required><br>

        <label for="password">Password:</label><br>
        <input type="password" name="password" required><br>

        <label for="role">Role:</label><br>
        <select name="role">
            <option value="1">Manager</option>
            <option value="2">Operator</option>
        </select><br><br>

        <input type="submit" value="Register">
    </form>

    <p>Already have an account? <a href="LoginPage.jsp">Login here</a></p>
</body>
</html>
