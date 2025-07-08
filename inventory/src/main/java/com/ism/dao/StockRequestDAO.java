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

    public void addStockRequest(StockRequest req) throws SQLException {
        String sql = "INSERT INTO STOCKREQUEST (REQ_ID, SUPPLIER_ID, USER_ID, QUANTITY_REQ, PRODUCT_REQ, DATE_REQ) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, req.getReqId());
            stmt.setLong(2, req.getSupplierId());
            stmt.setLong(3, req.getUserId());
            stmt.setLong(4, req.getQuantityReq());
            stmt.setString(5, req.getProductReq());
            stmt.setDate(6, new java.sql.Date(req.getDateReq().getTime()));
            stmt.executeUpdate();
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
