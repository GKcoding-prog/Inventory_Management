package com.ism.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ism.models.Supplier;
import com.ism.utils.DBConnect;
import com.ism.dao.SupplierDAO;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SuppliersPageController {
    @FXML private TableView<Supplier> suppliersTable;
    @FXML private TableColumn<Supplier, String> nameColumn;
    @FXML private TableColumn<Supplier, String> contactColumn;
    @FXML private Button returnBtn;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierFname"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        suppliersTable.setItems(getSupplierData());
        returnBtn.setOnAction(this::handleReturn);
        com.ism.models.User currentUser = null;
        try {
            currentUser = (com.ism.models.User) com.ism.controllers.ProfilePageController.getCurrentUser();
        } catch (Exception ignored) {}
        boolean isBoss = currentUser != null && "Boss".equals(currentUser.getRole());
        suppliersTable.setEditable(isBoss);
        addBtn.setDisable(!isBoss);
        editBtn.setDisable(!isBoss);
        deleteBtn.setDisable(!isBoss);
        addBtn.setOnAction(e -> handleAddSupplier());
        editBtn.setOnAction(e -> handleEditSupplier());
        deleteBtn.setOnAction(e -> handleDeleteSupplier());
    }

    private ObservableList<Supplier> getSupplierData() {
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            SupplierDAO dao = new SupplierDAO(conn);
            data.addAll(dao.getAllSuppliers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void handleReturn(ActionEvent event) {
        try {
            Stage stage = (Stage) returnBtn.getScene().getWindow();
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

    private void handleAddSupplier() {
        javafx.scene.control.Dialog<javafx.util.Pair<String, String>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Add Supplier");
        dialog.setHeaderText("Enter supplier details:");
        javafx.scene.control.ButtonType addButtonType = new javafx.scene.control.ButtonType("Add", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        nameField.setPromptText("Name");
        javafx.scene.control.TextField contactField = new javafx.scene.control.TextField();
        contactField.setPromptText("Contact Info");

        grid.add(new javafx.scene.control.Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Contact Info:"), 0, 1);
        grid.add(contactField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new javafx.util.Pair<>(nameField.getText(), contactField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String name = result.getKey().trim();
            String contact = result.getValue().trim();
            if (name.isEmpty() || contact.isEmpty()) {
                showError("Please enter both name and contact info.");
                return;
            }
            try (java.sql.Connection conn = DBConnect.getConnection()) {
                SupplierDAO dao = new SupplierDAO(conn);
                dao.addSupplier(new Supplier(0, name, contact));
                suppliersTable.setItems(getSupplierData());
                suppliersTable.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Failed to add supplier.");
            }
        });
    }

    private void handleEditSupplier() {
        Supplier selected = suppliersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a supplier to edit.");
            return;
        }
        javafx.scene.control.Dialog<javafx.util.Pair<String, String>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Edit Supplier");
        dialog.setHeaderText("Edit supplier details:");
        javafx.scene.control.ButtonType editButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField(selected.getSupplierFname());
        javafx.scene.control.TextField contactField = new javafx.scene.control.TextField(selected.getContactInfo());

        grid.add(new javafx.scene.control.Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Contact Info:"), 0, 1);
        grid.add(contactField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                return new javafx.util.Pair<>(nameField.getText(), contactField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String name = result.getKey().trim();
            String contact = result.getValue().trim();
            if (name.isEmpty() || contact.isEmpty()) {
                showError("Please enter both name and contact info.");
                return;
            }
            try (java.sql.Connection conn = DBConnect.getConnection()) {
                SupplierDAO dao = new SupplierDAO(conn);
                selected.setSupplierFname(name);
                selected.setContactInfo(contact);
                dao.updateSupplier(selected);
                suppliersTable.setItems(getSupplierData());
                suppliersTable.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Failed to update supplier.");
            }
        });
    }

    private void handleDeleteSupplier() {
        Supplier selected = suppliersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a supplier to delete.");
            return;
        }
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Supplier");
        alert.setHeaderText("Are you sure you want to delete this supplier?");
        alert.setContentText(selected.getSupplierFname());
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try (java.sql.Connection conn = DBConnect.getConnection()) {
                    SupplierDAO dao = new SupplierDAO(conn);
                    dao.deleteSupplier(selected.getSupplierId());
                    suppliersTable.setItems(getSupplierData());
                    suppliersTable.refresh();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Failed to delete supplier.");
                }
            }
        });
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
