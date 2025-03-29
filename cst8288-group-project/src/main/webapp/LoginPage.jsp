<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
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

        /* Form and Input Styling */
        label {
            display: block;
            margin-bottom: 8px;
            text-align: left;
            color: #555;
            font-weight: 600;
        }
        input[type="email"], input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
            outline: none;
            transition: border 0.3s ease-in-out;
        }
        input[type="email"]:focus, input[type="password"]:focus {
            border-color: #4682B4; /* Muted blue for input focus */
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
            text-decoration: none; /* Removed underline on hover */
        }
    </style>
</head>
<body>

    <!-- Header Section - Top Left Aligned -->
    <div class="header">
        <h1>Public Transit Fleet Management System</h1>
    </div>

    <!-- Login Container -->
    <div class="container">
        <h2>Login</h2>

        <%-- Display error message if login fails --%>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>

        <form method="post" action="LoginServlet">
            <label for="email">Email:</label>
            <input type="email" name="email" required>

            <label for="password">Password:</label>
            <input type="password" name="password" required>

            <input type="submit" value="Login">
        </form>

        <p>Don't have an account? <a href="RegisterPage.jsp">Register here</a></p>
    </div>

</body>
</html>
