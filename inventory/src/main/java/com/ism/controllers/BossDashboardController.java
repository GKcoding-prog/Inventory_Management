package com.ism.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BossDashboardController {
    @FXML private Button inventoryBtn;
    @FXML private Button manageUsersBtn;
    @FXML private Button manageCategoriesBtn;
    @FXML private Button stockRequestsBtn;
    @FXML private Button profileBtn;
    @FXML private Button logoutBtn;

    @FXML
    private void initialize() {
        inventoryBtn.setOnAction(e -> navigate("/com/ism/InventoryPage.fxml"));
        manageUsersBtn.setOnAction(e -> navigate("/com/ism/ManageUsersPage.fxml"));
        manageCategoriesBtn.setOnAction(e -> navigate("/com/ism/ManageCategoriesPage.fxml"));
        stockRequestsBtn.setOnAction(e -> navigate("/com/ism/StockRequestsPage.fxml"));
        profileBtn.setOnAction(e -> navigate("/com/ism/ProfilePage.fxml"));
        logoutBtn.setOnAction(e -> navigate("/com/ism/LoginPage.fxml"));
    }

    private void navigate(String fxml) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxml));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) inventoryBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception ex) {
            System.out.println("BossDashboardController: Navigation error: " + ex.getMessage());
        }
    }
}
