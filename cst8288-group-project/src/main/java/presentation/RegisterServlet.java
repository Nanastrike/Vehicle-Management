package presentation;

import data.UserDAO;
import model.User.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get user input from the form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int userTypeId = Integer.parseInt(request.getParameter("role"));

        System.out.println("Received registration request for email: " + email);

        // Hash the password before storing
        String passwordHash = hashPassword(password);
        System.out.println("Password hashed: " + passwordHash);

        // Create a User object
        User newUser = new User(0, name, email, passwordHash, userTypeId, null);

        // Create an instance of UserDAO
        UserDAO userDAO = new UserDAO();

        // Check if the email is already registered
        if (userDAO.isEmailRegistered(email)) {
            System.out.println("Email already registered: " + email);
            request.setAttribute("error", "Email already registered. Please login.");
            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
            return;
        }

        // Attempt to register the user
        boolean success = userDAO.registerUser(newUser);
        System.out.println("User registration success status: " + success);

        if (success) {
            System.out.println("User registered successfully: " + email);
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        } else {
            System.out.println("User registration failed for email: " + email);
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("RegisterPage.jsp").forward(request, response);
        }
    }

    // Hash password method
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback if hash fails (not secure)
        }
    }
}