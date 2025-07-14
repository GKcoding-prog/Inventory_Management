package com.ism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ism.models.Product;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO PRODUCTS (PRODUCT_ID, CAT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, product.getProductId());
            stmt.setLong(2, product.getCategoryId());
            stmt.setString(3, product.getProductName());
            stmt.setDouble(4, product.getProductPrice());
            stmt.setLong(5, product.getProductQuantity());
            int rowsInserted = stmt.executeUpdate();
            System.out.println("Product added successfully. Rows inserted: " + rowsInserted);
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM PRODUCTS";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getLong("PRODUCT_ID"),
                    rs.getLong("CAT_ID"),
                    rs.getString("PRODUCT_NAME"),
                    rs.getDouble("PRODUCT_PRICE"),
                    rs.getLong("PRODUCT_QUANTITY")
                ));
            }
        }
        return products;
    }
}