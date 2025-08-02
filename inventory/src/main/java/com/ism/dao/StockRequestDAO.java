package com.ism.dao;

import com.ism.models.StockRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockRequestDAO {
    public List<StockRequest> getAllRequests() throws SQLException {
        return getAllStockRequests();
    }

    public void approveRequest(long requestId) throws SQLException {
        String sql = "UPDATE stockrequest SET STATUS='Approved' WHERE REQ_ID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, requestId);
            stmt.executeUpdate();
        }
    }

    public void deleteRequest(long requestId) throws SQLException {
        String sql = "DELETE FROM stockrequest WHERE REQ_ID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, requestId);
            stmt.executeUpdate();
        }
    }
    private final Connection connection;

    public StockRequestDAO(Connection connection) {
        this.connection = connection;
    }

    public void addStockRequest(StockRequest request) throws SQLException {
        String sql = "INSERT INTO stockrequest (SUPPLIER_ID, USER_ID, QUANTITY_REQ, PRODUCT_REQ, DATE_REQ, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, request.getSupplierId());
            stmt.setLong(2, request.getUserId());
            stmt.setLong(3, request.getQuantity());
            stmt.setString(4, request.getProductName());
            stmt.setDate(5, (Date) request.getDateReq());
            stmt.setString(6, request.getStatus() == null ? "Pending" : request.getStatus());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        request.setReqId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    public List<StockRequest> getAllStockRequests() throws SQLException {
        String sql = "SELECT * FROM stockrequest";
        List<StockRequest> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                java.sql.Date date = rs.getDate("DATE_REQ");
                if (date == null || date.toString().equals("0000-00-00")) {
                    // Skip or handle zero date value
                    continue;
                }
                requests.add(new StockRequest(
                    rs.getLong("REQ_ID"),
                    rs.getLong("SUPPLIER_ID"),
                    rs.getLong("USER_ID"),
                    rs.getLong("QUANTITY_REQ"),
                    rs.getString("PRODUCT_REQ"),
                    date,
                    rs.getString("STATUS")
                ));
            }
        }
        return requests;
    }
}
