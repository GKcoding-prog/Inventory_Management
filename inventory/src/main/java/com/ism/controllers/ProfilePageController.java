package com.ism.controllers;

import com.ism.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfilePageController {
    @FXML private Label profileInfo;
    @FXML private Button returnBtn;
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    @FXML
    private void initialize() {
        if (currentUser != null) {
            String info = String.format("Name: %s\nEmail: %s\nRole: %s\nPhone: %s\nStatus: %s",
                currentUser.getFirstName(),
                currentUser.getEmail(),
                currentUser.getRole(),
                currentUser.getPhone(),
                currentUser.getStatus()
            );
            profileInfo.setText(info);
        } else {
            profileInfo.setText("No user information available.");
        }
        returnBtn.setOnAction(e -> returnToHome());
    }

    private void returnToHome() {
        try {
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            String dashboardFXML = "/com/ism/BossDashboard.fxml";
            User currentUser = ProfilePageController.getCurrentUser();
            if (currentUser != null) {
                String role = currentUser.getRole();
                switch (role) {
                    case "Boss":
                        dashboardFXML = "/com/ism/BossDashboard.fxml";
                        break;
                    case "Employee":
                        dashboardFXML = "/com/ism/EmployeeDashboard.fxml";
                        break;
                    case "Supplier":
                        dashboardFXML = "/com/ism/SupplierDashboard.fxml";
                        break;
                }
            }
            Parent root = FXMLLoader.load(getClass().getResource(dashboardFXML));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
