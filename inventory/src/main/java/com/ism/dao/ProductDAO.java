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
        String sql = "INSERT INTO PRODUCTS (CAT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, product.getCategoryId());
            stmt.setString(2, product.getProductName());
            stmt.setDouble(3, product.getProductPrice());
            stmt.setLong(4, product.getProductQuantity());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getLong(1));
                    }
                }
            }
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

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE PRODUCTS SET CAT_ID = ?, PRODUCT_NAME = ?, PRODUCT_PRICE = ?, PRODUCT_QUANTITY = ? WHERE PRODUCT_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, product.getCategoryId());
            stmt.setString(2, product.getProductName());
            stmt.setDouble(3, product.getProductPrice());
            stmt.setLong(4, product.getProductQuantity());
            stmt.setLong(5, product.getProductId());
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Product updated successfully. Rows updated: " + rowsUpdated);
        }
    }

    public void deleteProduct(long productId) throws SQLException {
        String sql = "DELETE FROM PRODUCTS WHERE PRODUCT_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, productId);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Product deleted successfully. Rows deleted: " + rowsDeleted);
        }
    }
}