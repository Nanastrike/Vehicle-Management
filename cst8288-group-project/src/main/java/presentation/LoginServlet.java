package presentation;

import data.UserDAO;
import model.User.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Handles login requests by validating user credentials and redirecting users
 * to the appropriate dashboard based on their role.
 *
 * <p>
 * This servlet receives POST requests from the login form, hashes the password
 * using SHA-256, and checks credentials through {@link data.UserDAO}. On
 * successful login, a session is created and the user is redirected to the main
 * dashboard.</p>
 *
 * <p>
 * If login fails, the user is forwarded back to the login page with an error
 * message.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 *
 * @see data.UserDAO
 * @see model.User.User
 * @see jakarta.servlet.http.HttpServlet
 */
public class LoginServlet extends HttpServlet {

    /**
     * Handles POST requests for user login.
     *
     * @param request the HttpServletRequest containing login form data
     * @param response the HttpServletResponse to send a redirect or forward
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an I/O error occurs
     */
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
            session.setAttribute("userID", user.getUserId());
            // Redirect to dashboard.jsp
            response.sendRedirect("VehicleManagementServlet?action=dashboard");
        } else {
            // Invalid login → Send error message to login.jsp
            request.setAttribute("error", "Invalid email or password. Please try again.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        }
    }

    /**
     * Utility method to hash passwords using SHA-256 algorithm.
     *
     * @param password the plain-text password to hash
     * @return the hashed password string in hexadecimal format
     */
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
