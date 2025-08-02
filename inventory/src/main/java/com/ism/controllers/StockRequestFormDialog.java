package com.ism.controllers;

import com.ism.models.StockRequest;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class StockRequestFormDialog extends Dialog<StockRequest> {
    private TextField productField = new TextField();
    private TextField quantityField = new TextField();
    private TextField supplierIdField = new TextField();
    private TextField userIdField = new TextField();
    private DatePicker datePicker = new DatePicker();

    public StockRequestFormDialog() {
        setTitle("Add Stock Request");
        setHeaderText("Enter new stock request details");
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;
        grid.add(new Label("Product Name:"), 0, row);
        grid.add(productField, 1, row);
        row++;
        grid.add(new Label("Quantity:"), 0, row);
        grid.add(quantityField, 1, row);
        row++;
        grid.add(new Label("Supplier ID:"), 0, row);
        grid.add(supplierIdField, 1, row);
        row++;
        grid.add(new Label("User ID:"), 0, row);
        grid.add(userIdField, 1, row);
        row++;
        grid.add(new Label("Request Date:"), 0, row);
        grid.add(datePicker, 1, row);

        getDialogPane().setContent(grid);
        setResultConverter(new Callback<ButtonType, StockRequest>() {
            @Override
            public StockRequest call(ButtonType buttonType) {
                if (buttonType == okButtonType) {
                    try {
                        String product = productField.getText();
                        int quantity = Integer.parseInt(quantityField.getText());
                        long supplierId = Long.parseLong(supplierIdField.getText());
                        long userId = Long.parseLong(userIdField.getText());
                        if (product.isEmpty() || quantity <= 0 || supplierId <= 0 || userId <= 0 || datePicker.getValue() == null) {
                            showError("All fields must be filled with valid values.");
                            return null;
                        }
                        java.sql.Date reqDate = java.sql.Date.valueOf(datePicker.getValue());
                        return new com.ism.models.StockRequest(0, supplierId, userId, quantity, product, reqDate, "Pending");
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
