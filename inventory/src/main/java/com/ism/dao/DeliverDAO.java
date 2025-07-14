package com.ism.dao;

import com.ism.models.Deliver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliverDAO {
    private final Connection connection;

    public DeliverDAO(Connection connection) {
        this.connection = connection;
    }

    public void addDeliver(Deliver deliver) throws SQLException {
        String sql = "INSERT INTO DELIVER (PRODUCT_ID, SUPPLIER_ID, DELIVERY_DATE) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, deliver.getProductId());
            stmt.setLong(2, deliver.getSupplierId());
            stmt.setDate(3, new java.sql.Date(deliver.getDeliveryDate().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Deliver> getAllDelivers() throws SQLException {
        String sql = "SELECT * FROM DELIVER";
        List<Deliver> delivers = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                delivers.add(new Deliver(
                    rs.getLong("PRODUCT_ID"),
                    rs.getLong("SUPPLIER_ID"),
                    rs.getDate("DELIVERY_DATE")
                ));
            }
        }
        return delivers;
    }
}
