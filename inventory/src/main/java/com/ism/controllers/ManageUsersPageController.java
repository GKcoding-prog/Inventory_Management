package com.ism.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ism.models.User;
import com.ism.utils.DBConnect;
import com.ism.dao.UserDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManageUsersPageController {
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private Button addUserBtn;
    @FXML private Button editUserBtn;
    @FXML private Button deleteUserBtn;
    @FXML private Button backBtn;

    @FXML
    private void initialize() {
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstName()));
        roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        usersTable.setItems(getUserData());
        addUserBtn.setOnAction(this::handleAddUser);
        editUserBtn.setOnAction(this::handleEditUser);
        deleteUserBtn.setOnAction(this::handleDeleteUser);
        backBtn.setOnAction(this::handleBack);
    }

    private ObservableList<User> getUserData() {
        ObservableList<User> data = FXCollections.observableArrayList();
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            UserDAO dao = new UserDAO(conn);
            data.addAll(dao.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        UserFormDialog dialog = new UserFormDialog(null);
        dialog.showAndWait().ifPresent(user -> {
            try (java.sql.Connection conn = DBConnect.getConnection()) {
                UserDAO dao = new UserDAO(conn);
                dao.addUser(user);
                usersTable.setItems(getUserData());
                usersTable.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Failed to add user. Please check your input and try again.");
            }
        });
    }

    @FXML
    private void handleEditUser(ActionEvent event) {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a user to edit.");
            return;
        }
        UserFormDialog dialog = new UserFormDialog(selected);
        dialog.showAndWait().ifPresent(updatedUser -> {
            try (java.sql.Connection conn = DBConnect.getConnection()) {
                UserDAO dao = new UserDAO(conn);
                dao.updateUser(updatedUser);
                usersTable.setItems(getUserData());
                usersTable.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Failed to update user. Please check your input and try again.");
            }
        });
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a user to delete.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText(selected.getFirstName());
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try (java.sql.Connection conn = DBConnect.getConnection()) {
                    UserDAO dao = new UserDAO(conn);
                    dao.deleteUser(selected.getUserId());
                    usersTable.setItems(getUserData());
                    usersTable.refresh();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Failed to delete user. Please try again.");
                }
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            String dashboardFXML = "/com/ism/BossDashboard.fxml";
            com.ism.models.User currentUser = null;
            try {
                currentUser = (com.ism.models.User) com.ism.controllers.ProfilePageController.getCurrentUser();
            } catch (Exception ignored) {}
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

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
