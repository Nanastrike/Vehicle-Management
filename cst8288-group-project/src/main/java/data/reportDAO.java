package data;

import model.Report.Report;
import model.User.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reportDAO {

    private final Connection conn;

    public reportDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert a new report into the database
    public boolean insertReport(Report report) throws SQLException {
        String sql = "INSERT INTO Reports (Title, Content, ReportType, CreatedBy, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, report.getTitle());
            stmt.setString(2, report.getContent());
            stmt.setString(3, report.getReportType());
            stmt.setString(4, report.getCreatedBy());
            stmt.setTimestamp(5, report.getCreatedAt());
            return stmt.executeUpdate() > 0;
        }
    }

    // Retrieve all reports
    public List<Report> getAllReports() throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Reports ORDER BY CreatedAt DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("ReportID"));
                report.setTitle(rs.getString("ReportTitle"));
                report.setContent(rs.getString("ReportContent"));
                report.setCreatedAt(rs.getTimestamp("CreatedAt"));
                reports.add(report);
            }
        }
        return reports;
    }

    // Get a single report by ID
    public Report getReportById(int id) throws SQLException {
        String sql = "SELECT * FROM Reports WHERE ReportID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("ReportID"));
                report.setTitle(rs.getString("ReportTitle"));
                report.setContent(rs.getString("ReportContent"));
                report.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return report;
            }
        }
        return null;
    }
    
    public Map<String, Integer> getReportTypeCounts() throws SQLException {
    String sql = "SELECT ReportType, COUNT(*) as Count FROM Reports GROUP BY ReportType";
    Map<String, Integer> counts = new HashMap<>();

    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            String type = rs.getString("ReportType");
            int count = rs.getInt("Count");
            counts.put(type, count);
        }
    }

    return counts;
}

}

