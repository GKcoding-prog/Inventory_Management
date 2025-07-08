package com.ism.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomePageController {

    @FXML
    private Button inventoryBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private void initialize() {
        inventoryBtn.setOnAction(this::handleInventory);
        profileBtn.setOnAction(this::handleProfile);
        logoutBtn.setOnAction(this::handleLogout);
    }

    @FXML
    private void handleInventory(ActionEvent event) {
        try {
            Stage stage = (Stage) inventoryBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ism/InventoryPage.fxml"));
            stage.setScene(new Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        try {
            Stage stage = (Stage) profileBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ism/ProfilePage.fxml"));
            stage.setScene(new Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ism/LoginPage.fxml"));
            stage.setScene(new Scene(root));
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }
    }
}