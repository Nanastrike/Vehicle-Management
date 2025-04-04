package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles user logout functionality by invalidating the session
 * and redirecting the user to the login page.
 *
 * <p>This servlet is typically triggered via a logout link or button
 * and ensures that any user-specific session data is cleared.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 1.21
 *
 * @see jakarta.servlet.http.HttpServlet
 * @see jakarta.servlet.http.HttpSession
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests for user logout.
     *
     * <p>Invalidates the current user session if it exists
     * and redirects the user to the login page.</p>
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirect to login page
        response.sendRedirect("LoginPage.jsp");
    }
}
