package com.ism.dao;

import com.ism.models.StockRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockRequestDAO {
    private final Connection connection;

    public StockRequestDAO(Connection connection) {
        this.connection = connection;
    }

    public void addStockRequest(StockRequest request) throws SQLException {
        String sql = "INSERT INTO STOCKREQUEST (SUPPLIER_ID, USER_ID, QUANTITY_REQ, PRODUCT_REQ, DATE_REQ) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, request.getSupplierId());
            stmt.setLong(2, request.getUserId());
            stmt.setLong(3, request.getQuantityReq());
            stmt.setString(4, request.getProductReq());
            stmt.setDate(5, (Date) request.getDateReq());
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
        String sql = "SELECT * FROM STOCKREQUEST";
        List<StockRequest> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(new StockRequest(
                    rs.getLong("REQ_ID"),
                    rs.getLong("SUPPLIER_ID"),
                    rs.getLong("USER_ID"),
                    rs.getLong("QUANTITY_REQ"),
                    rs.getString("PRODUCT_REQ"),
                    rs.getDate("DATE_REQ")
                ));
            }
        }
        return requests;
    }
}
