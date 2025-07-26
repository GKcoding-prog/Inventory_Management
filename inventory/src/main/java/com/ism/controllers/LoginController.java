package com.ism.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import com.ism.dao.UserDAO;
import com.ism.models.User;
import com.ism.utils.DBConnect;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @FXML
    private void initialize() {
        try {
            if (userDAO == null) {
                Connection conn = DBConnect.getConnection();
                userDAO = new UserDAO(conn);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not connect to the database. Please contact support.");
            loginButton.setDisable(true);
        }
        loginButton.setOnAction(e -> onLogin());
    }

    private void onLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim().toLowerCase();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please enter both email and password.");
            return;
        }
        try {
            User user = userDAO.getUserByEmailAndPassword(email, password);
            System.out.println("LoginController: user object after query: " + user);
            if (user != null) {
                System.out.println("LoginController: userId=" + user.getUserId() + ", role=" + user.getRole() + ", email=" + user.getEmail());
                com.ism.controllers.ProfilePageController.setCurrentUser(user);
                String dashboardFXML = "/com/ism/BossDashboard.fxml";
                try {
                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(dashboardFXML));
                    javafx.scene.Parent root = loader.load();
                    javafx.stage.Stage stage = (javafx.stage.Stage) loginButton.getScene().getWindow();
                    stage.setScene(new javafx.scene.Scene(root));
                } catch (Exception ex) {
                    System.out.println("LoginController: Exception loading " + dashboardFXML + ": " + ex.getMessage());
                    showAlert("Error", "Failed to load dashboard: " + ex.getMessage());
                }
            } else {
                System.out.println("LoginController: user is null after query");
                showAlert("Login Failed", "Invalid credentials! Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("LoginController: SQLException: " + e.getMessage());
            showAlert("Database Error", "A database error occurred. Please try again later.");
        }
    }

    private void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}
