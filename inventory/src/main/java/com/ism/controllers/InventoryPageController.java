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

    @FXML
    private void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        inventoryTable.setItems(getRealData());
        returnBtn.setOnAction(e -> returnToHome());
    }

    private ObservableList<ProductRow> getRealData() {
        ObservableList<ProductRow> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection()) {
            ProductDAO productDAO = new ProductDAO(conn);
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            SupplierDAO supplierDAO = new SupplierDAO(conn);
            List<Product> products = productDAO.getAllProducts();
            Map<Long, String> categoryMap = new HashMap<>();
            for (Category cat : categoryDAO.getAllCategories()) {
                categoryMap.put(cat.getCatId(), cat.getNameCat());
            }
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            String supplierName = suppliers.isEmpty() ? "N/A" : suppliers.get(0).getSupplierFname();
            for (Product p : products) {
                String category = categoryMap.getOrDefault(p.getCategoryId(), "Unknown");
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
        return data;
    }

    private void returnToHome() {
        try {
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ism/HomePage.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ProductRow {
        private final String name;
        private final String category;
        private final int quantity;
        private final String supplier;
        public ProductRow(String name, String category, int quantity, String supplier) {
            this.name = name;
            this.category = category;
            this.quantity = quantity;
            this.supplier = supplier;
        }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getQuantity() { return quantity; }
        public String getSupplier() { return supplier; }
    }
}
