package com.ism.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ism.dao.CategoryDAO;
import com.ism.dao.ProductDAO;
import com.ism.dao.SupplierDAO;
import com.ism.models.Category;
import com.ism.models.Product;
import com.ism.models.Supplier;
import com.ism.utils.DBConnect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InventoryPageController {
    @FXML private TableView<ProductRow> inventoryTable;
    @FXML private TableColumn<ProductRow, String> colName;
    @FXML private TableColumn<ProductRow, String> colCategory;
    @FXML private TableColumn<ProductRow, Integer> colQuantity;
    @FXML private TableColumn<ProductRow, String> colSupplier;
    @FXML private Button returnBtn;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    @FXML
    private void initialize() {
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        inventoryTable.setItems(getRealData());
        returnBtn.setOnAction(e -> returnToHome());
        addBtn.setOnAction(e -> handleAddProduct());
        editBtn.setOnAction(e -> handleEditProduct());
        deleteBtn.setOnAction(e -> handleDeleteProduct());
    }

    private ObservableList<ProductRow> getRealData() {
        ObservableList<ProductRow> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection()) {
            ProductDAO productDAO = new ProductDAO(conn);
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            SupplierDAO supplierDAO = new SupplierDAO(conn);
            List<Product> products = productDAO.getAllProducts();
            System.out.println("DEBUG: Products from DB: " + products.size());
            Map<Long, String> categoryMap = new HashMap<>();
            for (Category cat : categoryDAO.getAllCategories()) {
                categoryMap.put(cat.getCatId(), cat.getNameCat());
            }
            System.out.println("DEBUG: Categories from DB: " + categoryMap.size());
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            System.out.println("DEBUG: Suppliers from DB: " + suppliers.size());
            String supplierName = suppliers.isEmpty() ? "N/A" : suppliers.get(0).getSupplierFname();
            for (Product p : products) {
                String category = categoryMap.getOrDefault(p.getCategoryId(), "Unknown");
                System.out.println("DEBUG: Product: " + p.getProductName() + ", CatID: " + p.getCategoryId() + ", Category: " + category + ", Qty: " + p.getProductQuantity());
                data.add(new ProductRow(
                    p.getProductName(),
                    category,
                    (int)p.getProductQuantity(),
                    supplierName
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DEBUG: ProductRow list size: " + data.size());
        return data;
    }

    private Product getProductByRow(ProductRow row) {
        try (Connection conn = DBConnect.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            // Use product name and quantity for more robust matching
            for (Product p : dao.getAllProducts()) {
                if (p.getProductName().equals(row.getName()) &&
                    p.getProductQuantity() == row.getQuantity()) {
                    return p;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void handleAddProduct() {
        ProductFormDialog dialog = new ProductFormDialog(null);
        dialog.showAndWait().ifPresent(product -> {
            try (Connection conn = DBConnect.getConnection()) {
                ProductDAO dao = new ProductDAO(conn);
                dao.addProduct(product); // productId will be set by DB
                inventoryTable.setItems(getRealData());
                inventoryTable.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Failed to add product. Please check your input and try again.");
            }
        });
    }

    private void handleEditProduct() {
        ProductRow selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product to edit.");
            return;
        }
        Product product = getProductByRow(selected);
        if (product == null) {
            showError("Selected product not found. Please refresh and try again.");
            return;
        }
        ProductFormDialog dialog = new ProductFormDialog(product);
        dialog.showAndWait().ifPresent(updatedProduct -> {
            try (Connection conn = DBConnect.getConnection()) {
                ProductDAO dao = new ProductDAO(conn);
                dao.updateProduct(updatedProduct);
                inventoryTable.setItems(getRealData());
                inventoryTable.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Failed to update product. Please check your input and try again.");
            }
        });
    }

    private void handleDeleteProduct() {
        ProductRow selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a product to delete.");
            return;
        }
        Product product = getProductByRow(selected);
        if (product == null) {
            showError("Selected product not found. Please refresh and try again.");
            return;
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText(selected.getName());
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try (Connection conn = DBConnect.getConnection()) {
                    ProductDAO dao = new ProductDAO(conn);
                    dao.deleteProduct(product.getProductId());
                    inventoryTable.setItems(getRealData());
                    inventoryTable.refresh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showError("Failed to delete product. Please try again.");
                }
            }
        });
    }

    // Show error dialog for smoother UX
    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void returnToHome() {
        try {
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            String dashboardFXML = "/com/ism/BossDashboard.fxml";
            // if (com.ism.controllers.ProfilePageController.getCurrentUser() != null) {
            //     // Replace 'User' with the actual class name that defines getRole()
            //     com.ism.models.User currentUser = (com.ism.models.User) com.ism.controllers.ProfilePageController.getCurrentUser();
            //     String role = currentUser.getRole();
            //     switch (role) {
            //         case "Boss":
            //             dashboardFXML = "/com/ism/BossDashboard.fxml";
            //             break;
            //         case "Employee":
            //             dashboardFXML = "/com/ism/EmployeeDashboard.fxml";
            //             break;
            //         case "Supplier":
            //             dashboardFXML = "/com/ism/SupplierDashboard.fxml";
            //             break;
            //     }
            // }
            Parent root = FXMLLoader.load(getClass().getResource(dashboardFXML));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ProductRow {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty category;
        private final javafx.beans.property.SimpleIntegerProperty quantity;
        private final javafx.beans.property.SimpleStringProperty supplier;

        public ProductRow(String name, String category, int quantity, String supplier) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.category = new javafx.beans.property.SimpleStringProperty(category);
            this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
            this.supplier = new javafx.beans.property.SimpleStringProperty(supplier);
        }
        public String getName() { return name.get(); }
        public String getCategory() { return category.get(); }
        public int getQuantity() { return quantity.get(); }
        public String getSupplier() { return supplier.get(); }

        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty categoryProperty() { return category; }
        public javafx.beans.property.IntegerProperty quantityProperty() { return quantity; }
        public javafx.beans.property.StringProperty supplierProperty() { return supplier; }
    }
}
