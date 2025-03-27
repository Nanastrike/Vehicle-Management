package presentation;

import data.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve email and raw password from the form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hash the entered password before checking with the database
        String passwordHash = hashPassword(password);

        // Call UserDAO to check credentials using the hashed password
        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateLogin(email, passwordHash);

        if (user != null) {
            // Successful login → Create session
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);

            // Redirect to dashboard.jsp
            response.sendRedirect("DashboardPage.jsp");
        } else {
            // Invalid login → Send error message to login.jsp
            request.setAttribute("error", "Invalid email or password. Please try again.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        }
    }

    // Utility method to hash passwords using SHA-256
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
            return password; // fallback (not secure, but avoids crashes)
        }
    }
}
