package com.ism.controllers;

import com.ism.models.Product;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class ProductFormDialog extends Dialog<Product> {
    private TextField nameField = new TextField();
    private TextField categoryIdField = new TextField();
    private TextField priceField = new TextField();
    private TextField quantityField = new TextField();
    private TextField productIdField = new TextField();

    public ProductFormDialog(Product product) {
        setTitle(product == null ? "Add Product" : "Edit Product");
        setHeaderText(product == null ? "Enter new product details" : "Edit product details");
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;
        if (product != null) {
            grid.add(new Label("Product ID:"), 0, row);
            grid.add(productIdField, 1, row);
            row++;
        }
        grid.add(new Label("Name:"), 0, row);
        grid.add(nameField, 1, row);
        row++;
        grid.add(new Label("Category ID:"), 0, row);
        grid.add(categoryIdField, 1, row);
        row++;
        grid.add(new Label("Price:"), 0, row);
        grid.add(priceField, 1, row);
        row++;
        grid.add(new Label("Quantity:"), 0, row);
        grid.add(quantityField, 1, row);

        if (product != null) {
            productIdField.setText(String.valueOf(product.getProductId()));
            productIdField.setDisable(true); // Don't allow changing ID on edit
            nameField.setText(product.getProductName());
            categoryIdField.setText(String.valueOf(product.getCategoryId()));
            priceField.setText(String.valueOf(product.getProductPrice()));
            quantityField.setText(String.valueOf(product.getProductQuantity()));
        }

        getDialogPane().setContent(grid);
        setResultConverter(new Callback<ButtonType, Product>() {
            @Override
            public Product call(ButtonType buttonType) {
                if (buttonType == okButtonType) {
                    try {
                        long catId = Long.parseLong(categoryIdField.getText());
                        if (!categoryExists(catId)) {
                            showError("Category ID does not exist. Please enter a valid category ID.");
                            return null;
                        }
                        String name = nameField.getText();
                        double price = Double.parseDouble(priceField.getText());
                        long qty = Long.parseLong(quantityField.getText());
                        if (product != null) {
                            long id = Long.parseLong(productIdField.getText());
                            return new Product(id, catId, name, price, qty);
                        } else {
                            return new Product(0, catId, name, price, qty); // ID will be set by DB
                        }
                    } catch (Exception e) {
                        showError("Invalid input. Please check your entries.");
                        return null;
                    }
                }
                return null;
            }
        });
    }

    private boolean categoryExists(long catId) {
        try (java.sql.Connection conn = com.ism.utils.DBConnect.getConnection()) {
            com.ism.dao.CategoryDAO dao = new com.ism.dao.CategoryDAO(conn);
            for (com.ism.models.Category cat : dao.getAllCategories()) {
                if (cat.getCatId() == catId) return true;
            }
        } catch (Exception e) {
            // Ignore, treat as not found
        }
        return false;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
