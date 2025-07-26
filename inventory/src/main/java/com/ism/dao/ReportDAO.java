package com.ism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ism.models.Report;

public class ReportDAO {
    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    public void addReport(Report report) throws SQLException {
        String sql = "INSERT INTO REPORT (USER_ID, GENERATED_AT, CONTENT) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, report.getUserId());
            stmt.setDate(2, new java.sql.Date(report.getGeneratedAt().getTime()));
            stmt.setString(3, report.getContent());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setReportId(generatedKeys.getLong(1));
                    }
                }
            }
            System.out.println("Report added successfully. Rows inserted: " + rowsInserted);
        }
    }

    public List<Report> getReportsByUserId(long userId) throws SQLException {
        String sql = "SELECT * FROM REPORT WHERE USER_ID = ?";
        List<Report> reports = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(new Report(
                            rs.getLong("REPORT_ID"),
                            rs.getLong("USER_ID"),
                            rs.getDate("GENERATED_AT"),
                            rs.getString("CONTENT")
                    ));
                }
            }
        }
        return reports;
    }
}
