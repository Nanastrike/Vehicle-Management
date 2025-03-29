<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Register</title>
    <!-- Google Font - Quicksand for consistency -->
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;600&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        /* Header Styles */
        .header {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            background-color: #fff;
            padding: 15px 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
        .header h1 {
            margin: 0;
            font-size: 20px;
            color: #4682B4; /* Muted Steel Blue */
            font-weight: 600;
        }

        /* Container and Card Styling */
        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
            margin-top: 60px; /* Extra space to account for header */
        }
        h2 {
            margin-bottom: 20px;
            color: #555;
        }
        .error {
            color: red;
            margin-bottom: 15px;
            font-size: 14px;
        }
        .success {
            color: green;
            margin-bottom: 15px;
            font-size: 14px;
        }

        /* Form and Input Styling */
        label {
            display: block;
            margin-bottom: 8px;
            text-align: left;
            color: #555;
            font-weight: 600;
        }
        input[type="text"], input[type="email"], input[type="password"], select {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
            outline: none;
            transition: border 0.3s ease-in-out;
        }
        input[type="text"]:focus, input[type="email"]:focus, input[type="password"]:focus, select:focus {
            border-color: #4682B4;
        }

        /* Dropdown Styling */
        select {
            appearance: none;
            background-color: #fff;
            cursor: pointer;
        }

        /* Button Styling */
        input[type="submit"] {
            background-color: #4682B4; /* Muted Blue */
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            width: 100%;
            transition: background-color 0.3s ease-in-out;
        }
        input[type="submit"]:hover {
            background-color: #4169E1; /* Royal Blue for hover */
        }

        /* Links and Footer */
        p {
            margin-top: 20px;
            font-size: 14px;
            color: #555;
        }
        a {
            color: #4682B4;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s;
        }
        a:hover {
            color: #4169E1;
            text-decoration: none;
        }
    </style>
</head>
<body>

    <!-- Header Section - Top Left Aligned -->
    <div class="header">
        <h1>Public Transit Fleet Management System</h1>
    </div>

    <!-- Registration Container -->
    <div class="container">
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
            <label for="name">Name:</label>
            <input type="text" name="name" required>

            <label for="email">Email:</label>
            <input type="email" name="email" required>

            <label for="password">Password:</label>
            <input type="password" name="password" required>

            <label for="role">Role:</label>
            <select name="role" required>
                <option value="1">Manager</option>
                <option value="2">Operator</option>
            </select>

            <input type="submit" value="Register">
        </form>

        <p>Already have an account? <a href="LoginPage.jsp">Login here</a></p>
    </div>

</body>
</html>
