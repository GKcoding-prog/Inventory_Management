package com.ism.dao;

import com.ism.models.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private final Connection connection;

    public SupplierDAO(Connection connection) {
        this.connection = connection;
    }

    public void addSupplier(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO SUPPLIER (SUPPLIER_FNAME, CONTACT_INFO) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplier.getSupplierFname());
            stmt.setString(2, supplier.getContactInfo());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        supplier.setSupplierId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        String sql = "SELECT * FROM SUPPLIER";
        List<Supplier> suppliers = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getLong("SUPPLIER_ID"),
                    rs.getString("SUPPLIER_FNAME"),
                    rs.getString("CONTACT_INFO")
                ));
            }
        }
        return suppliers;
    }
}
