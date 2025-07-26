package com.ism.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ism.models.Boss;
import com.ism.models.Employee;
import com.ism.models.User;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO USERS (USER_FNAME, USER_EMAIL, USER_PWD, USER_ROLE, USER_PHONE, USER_STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getStatus());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getLong(1));
                    }
                }
            }
            System.out.println("User added successfully. Rows inserted: " + rowsInserted);
        }
    }

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE USER_EMAIL = ? AND USER_PWD = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            System.out.println("Executing query: " + sql); // Debugging statement
            System.out.println("With parameters: email=" + email + ", password=" + password); // Debugging statement
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found with email: " + email); // Debugging statement
                    String role = rs.getString("USER_ROLE");
                    String phone = rs.getString("USER_PHONE");
                    String status = rs.getString("USER_STATUS");
                    if ("Boss".equals(role)) {
                        return new Boss(
                                rs.getLong("USER_ID"),
                                rs.getString("USER_FNAME"),
                                rs.getString("USER_EMAIL"),
                                rs.getString("USER_PWD"),
                                phone,
                                status
                        );
                    } else if ("Employee".equals(role)) {
                        return new Employee(
                                rs.getLong("USER_ID"),
                                rs.getString("USER_FNAME"),
                                rs.getString("USER_EMAIL"),
                                rs.getString("USER_PWD"),
                                phone,
                                status
                        );
                    } else {
                        // Fallback: return an Employee for unknown roles
                        return new Employee(
                                rs.getLong("USER_ID"),
                                rs.getString("USER_FNAME"),
                                rs.getString("USER_EMAIL"),
                                rs.getString("USER_PWD"),
                                phone,
                                status
                        );
                    }
                } else {
                    System.out.println("No user found with email: " + email); // Debugging statement
                }
            }
        }
        return null;
    }
}
