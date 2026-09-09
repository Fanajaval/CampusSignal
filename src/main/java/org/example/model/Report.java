package org.example.model;

import java.time.LocalDateTime;

public class Report {
    private long id;
    private String title;
    private String description;
    private String location;
    private String reporter;
    private ReportCategory category;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private boolean updated;
    private LocalDateTime updatedAt;

    public Report() {
    }

    public Report(long id, String title, String description, String location, String reporter,
                  ReportCategory category, ReportStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.reporter = reporter;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.updated = false;
        this.updatedAt = null;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getReporter() { return reporter; }
    public void setReporter(String reporter) { this.reporter = reporter; }
    public ReportCategory getCategory() { return category; }
    public void setCategory(ReportCategory category) { this.category = category; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isUpdated() { return updated; }
    public void setUpdated(boolean updated) { this.updated = updated; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}