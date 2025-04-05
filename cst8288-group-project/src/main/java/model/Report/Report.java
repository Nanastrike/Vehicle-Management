package model.Report;

import java.sql.Timestamp;

/**
 * Represents a report entity in the system.
 * A report contains a title, type, content, creator information, and a timestamp indicating when it was created.
 * 
 * <p>This model is used throughout the application to transfer report-related data.</p>
 * 
 * @author Zhennan
 * @version 1.0
 * @since Java 21
 */
public class Report {

    /**
     * The unique identifier for the report.
     */
    private int reportId;

    /**
     * The title of the report.
     */
    private String title;

    /**
     * The type/category of the report (e.g., Maintenance, Fuel, Operator).
     */
    private String reportType;

    /**
     * The content or body of the report.
     */
    private String content;

    /**
     * The username or identifier of the user who created the report.
     */
    private String createdBy;

    /**
     * The timestamp indicating when the report was created.
     */
    private Timestamp createdAt;

    /**
     * Gets the report ID.
     *
     * @return the report ID
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * Sets the report ID.
     *
     * @param reportId the ID to set
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    /**
     * Gets the title of the report.
     *
     * @return the report title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the report.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the type of the report.
     *
     * @return the report type
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * Sets the type of the report.
     *
     * @param reportType the type to set
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * Gets the content of the report.
     *
     * @return the report content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the report.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the creator of the report.
     *
     * @return the username or ID of the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the report.
     *
     * @param createdBy the username or ID to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the timestamp of when the report was created.
     *
     * @return the creation timestamp
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp of when the report was created.
     *
     * @param createdAt the timestamp to set
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
