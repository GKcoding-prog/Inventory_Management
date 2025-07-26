package com.ism.controllers;

import com.ism.dao.CategoryDAO;
import com.ism.models.Category;
import com.ism.utils.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.SQLException;

public class ManageCategoriesPageController {
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Long> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button returnBtn;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("catId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nameCat"));
        categoryTable.setItems(getCategoryData());
        addBtn.setOnAction(e -> handleAddCategory());
        editBtn.setOnAction(e -> handleEditCategory());
        deleteBtn.setOnAction(e -> handleDeleteCategory());
        returnBtn.setOnAction(e -> returnToDashboard());
    }

    private ObservableList<Category> getCategoryData() {
        ObservableList<Category> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection()) {
            CategoryDAO dao = new CategoryDAO(conn);
            data.addAll(dao.getAllCategories());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private void handleAddCategory() {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter new category name:");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showError("Category name cannot be empty.");
                return;
            }
            try (Connection conn = DBConnect.getConnection()) {
                CategoryDAO dao = new CategoryDAO(conn);
                dao.addCategory(new Category(0, name));
                categoryTable.setItems(getCategoryData());
                categoryTable.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Failed to add category.");
            }
        });
    }

    private void handleEditCategory() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a category to edit.");
            return;
        }
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog(selected.getNameCat());
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Edit category name:");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showError("Category name cannot be empty.");
                return;
            }
            try (Connection conn = DBConnect.getConnection()) {
                CategoryDAO dao = new CategoryDAO(conn);
                selected.setNameCat(name);
                dao.updateCategory(selected);
                categoryTable.setItems(getCategoryData());
                categoryTable.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Failed to update category.");
            }
        });
    }

    private void handleDeleteCategory() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a category to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Category");
        alert.setHeaderText("Are you sure you want to delete this category?");
        alert.setContentText(selected.getNameCat());
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try (Connection conn = DBConnect.getConnection()) {
                    CategoryDAO dao = new CategoryDAO(conn);
                    dao.deleteCategory(selected.getCatId());
                    categoryTable.setItems(getCategoryData());
                    categoryTable.refresh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showError("Failed to delete category.");
                }
            }
        });
    }

    private void returnToDashboard() {
        try {
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ism/BossDashboard.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
