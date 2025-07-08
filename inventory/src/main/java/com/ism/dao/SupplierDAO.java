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
        String sql = "INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_FNAME, CONTACT_INFO) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, supplier.getSupplierId());
            stmt.setString(2, supplier.getSupplierFname());
            stmt.setString(3, supplier.getContactInfo());
            stmt.executeUpdate();
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
