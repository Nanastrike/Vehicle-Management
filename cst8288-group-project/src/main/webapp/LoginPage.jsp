<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        .error { color: red; }
    </style>
</head>
<body>
    <h2>Login</h2>

    <%-- Display error message if login fails --%>
    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="post" action="LoginServlet">
        <label for="email">Email:</label><br>
        <input type="email" name="email" required><br>

        <label for="password">Password:</label><br>
        <input type="password" name="password" required><br><br>

        <input type="submit" value="Login">
    </form>

    <p>Don't have an account? <a href="RegisterPage.jsp">Register here</a></p>
</body>
</html>
