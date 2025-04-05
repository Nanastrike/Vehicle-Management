package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

@WebServlet("/DownloadReportServlet")
public class DownloadReportServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("reportTitle");
        String content = request.getParameter("reportContent");

        if (title == null || content == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing report data.");
            return;
        }

        // Clean filename
        String filename = URLEncoder.encode(title.replaceAll("\\s+", "_") + ".txt", "UTF-8");

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter out = response.getWriter()) {
            out.println("=== " + title + " ===\n");
            out.println(content);
        }
    }
}
