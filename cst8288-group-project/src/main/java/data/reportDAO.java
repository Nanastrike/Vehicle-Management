package data;

import model.Report.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) for managing report-related operations in the database.
 * Provides methods for inserting, retrieving, and analyzing reports.
 *
 * <p>This class interacts with the Reports table in the database to perform CRUD operations
 * and generate analytical data such as report type counts.</p>
 *
 * @author Zhennan Deng
 * @version 1.0
 * @since Java 21
 * @see model.Report.Report
 */
public class reportDAO {

    private final Connection conn;

    /**
     * Constructs a new reportDAO with the provided database connection.
     *
     * @param conn the active database connection
     */
    public reportDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new report into the Reports table.
     *
     * @param report the report object to insert
     * @return true if the insert was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves all reports from the Reports table, ordered by creation time descending.
     *
     * @return a list of all reports
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves a report from the Reports table by its ID.
     *
     * @param id the ID of the report to retrieve
     * @return the report object, or null if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves a count of reports grouped by report type.
     *
     * @return a map where the key is the report type and the value is the count
     * @throws SQLException if a database access error occurs
     */
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
