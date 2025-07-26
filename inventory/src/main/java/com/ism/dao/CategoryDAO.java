package com.ism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ism.models.Category;

public class CategoryDAO {
    private final Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO CATEGORY (NAME_CAT) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getNameCat());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCatId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE CATEGORY SET NAME_CAT = ? WHERE CAT_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getNameCat());
            stmt.setLong(2, category.getCatId());
            stmt.executeUpdate();
        }
    }

    public void deleteCategory(long catId) throws SQLException {
        String sql = "DELETE FROM CATEGORY WHERE CAT_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, catId);
            stmt.executeUpdate();
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT * FROM CATEGORY";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                    rs.getLong("CAT_ID"),
                    rs.getString("NAME_CAT")
                ));
            }
        }
        return categories;
    }
}
