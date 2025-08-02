package com.ism.controllers;

import com.ism.models.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class UserFormDialog extends Dialog<User> {
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextField roleField = new TextField();
    private TextField phoneField = new TextField();
    private TextField statusField = new TextField();
    private TextField userIdField = new TextField();

    public UserFormDialog(User user) {
        setTitle(user == null ? "Add User" : "Edit User");
        setHeaderText(user == null ? "Enter new user details" : "Edit user details");
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;
        if (user != null) {
            grid.add(new Label("User ID:"), 0, row);
            grid.add(userIdField, 1, row);
            userIdField.setText(String.valueOf(user.getUserId()));
            userIdField.setDisable(true);
            row++;
        }
        grid.add(new Label("Name:"), 0, row);
        grid.add(nameField, 1, row);
        if (user != null) nameField.setText(user.getFirstName());
        row++;
        grid.add(new Label("Email:"), 0, row);
        grid.add(emailField, 1, row);
        if (user != null) emailField.setText(user.getEmail());
        row++;
        grid.add(new Label("Password:"), 0, row);
        grid.add(passwordField, 1, row);
        if (user != null) passwordField.setText(user.getPassword());
        row++;
        grid.add(new Label("Role:"), 0, row);
        grid.add(roleField, 1, row);
        if (user != null) roleField.setText(user.getRole());
        row++;
        grid.add(new Label("Phone:"), 0, row);
        grid.add(phoneField, 1, row);
        if (user != null) phoneField.setText(user.getPhone());
        row++;
        grid.add(new Label("Status:"), 0, row);
        grid.add(statusField, 1, row);
        if (user != null) statusField.setText(user.getStatus());

        getDialogPane().setContent(grid);
        setResultConverter(new Callback<ButtonType, User>() {
            @Override
            public User call(ButtonType buttonType) {
                if (buttonType == okButtonType) {
                    try {
                        String name = nameField.getText();
                        String email = emailField.getText();
                        String password = passwordField.getText();
                        String role = roleField.getText();
                        String phone = phoneField.getText();
                        String status = statusField.getText();
                        long id = user != null ? user.getUserId() : 0;
                        // For simplicity, always return an Employee (can be extended for Boss/Supplier)
                        return new com.ism.models.Employee(id, name, email, password, phone, status);
                    } catch (Exception e) {
                        showError("Invalid input. Please check your entries.");
                        return null;
                    }
                }
                return null;
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
