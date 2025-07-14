package com.ism.utils;

import java.sql.Connection;

public class DBConnectTest {
    public static void main(String[] args) {
        try (Connection connection = DBConnect.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful! 🎉");
            } else {
                System.out.println("Failed to connect to the database. ❌");
            }
        } catch (Exception e) {
            System.err.println("Error while connecting to the database:");
        }
    }
}