package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * Servlet that handles report downloading functionality.
 * 
 * <p>This servlet accepts POST requests containing a report title and content,
 * and returns a plain text file for download with the report data.</p>
 *
 * <p>Expected parameters:</p>
 * <ul>
 *   <li><code>reportTitle</code> - the title of the report</li>
 *   <li><code>reportContent</code> - the main content/body of the report</li>
 * </ul>
 * 
 * <p>If either parameter is missing, a 400 Bad Request error is returned.</p>
 * 
 * @author Zhennan
 * @version 1.0
 * @since Java 21
 */
@WebServlet("/DownloadReportServlet")
public class DownloadReportServlet extends HttpServlet {

    /**
     * Handles POST requests to download a report as a text file.
     *
     * @param request  the HTTP request containing report title and content
     * @param response the HTTP response that triggers the file download
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("reportTitle");
        String content = request.getParameter("reportContent");

        if (title == null || content == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing report data.");
            return;
        }

        // Clean filename by replacing spaces and encoding
        String filename = URLEncoder.encode(title.replaceAll("\\s+", "_") + ".txt", "UTF-8");

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter out = response.getWriter()) {
            out.println("=== " + title + " ===\n");
            out.println(content);
        }
    }
}
