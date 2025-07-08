package com.ism.models;

import java.util.Date;

public class Report {
    private long reportId;
    private long userId;
    private Date generatedAt;
    private String content;

    public Report(long reportId, long userId, Date generatedAt, String content) {
        this.reportId = reportId;
        this.userId = userId;
        this.generatedAt = generatedAt;
        this.content = content;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
