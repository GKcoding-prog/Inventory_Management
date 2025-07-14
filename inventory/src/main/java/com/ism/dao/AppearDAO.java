package com.ism.dao;

import com.ism.models.Appear;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppearDAO {
    private final Connection connection;

    public AppearDAO(Connection connection) {
        this.connection = connection;
    }

    public void addAppear(Appear appear) throws SQLException {
        String sql = "INSERT INTO APPEAR (PRODUCT_ID, REQ_ID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, appear.getProductId());
            stmt.setLong(2, appear.getReqId());
            stmt.executeUpdate();
        }
    }

    public List<Appear> getAllAppears() throws SQLException {
        String sql = "SELECT * FROM APPEAR";
        List<Appear> appears = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appears.add(new Appear(
                    rs.getLong("PRODUCT_ID"),
                    rs.getLong("REQ_ID")
                ));
            }
        }
        return appears;
    }
}
