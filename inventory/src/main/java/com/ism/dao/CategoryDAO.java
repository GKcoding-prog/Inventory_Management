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
        String sql = "INSERT INTO CATEGORY (CAT_ID, NAME_CAT) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, category.getCatId());
            stmt.setString(2, category.getNameCat());
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
