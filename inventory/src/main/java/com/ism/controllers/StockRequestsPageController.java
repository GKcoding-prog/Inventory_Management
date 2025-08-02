package com.ism.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ism.models.StockRequest;
import com.ism.utils.DBConnect;
import com.ism.dao.StockRequestDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StockRequestsPageController {
    @FXML private TableView<StockRequest> requestsTable;
    @FXML private TableColumn<StockRequest, String> productColumn;
    @FXML private TableColumn<StockRequest, Integer> quantityColumn;
    @FXML private TableColumn<StockRequest, String> dateColumn;
    @FXML private TableColumn<StockRequest, String> userNameColumn;
    @FXML private TableColumn<StockRequest, String> supplierNameColumn;
    @FXML private TableColumn<StockRequest, String> statusColumn;
    @FXML private Button addRequestBtn;
    @FXML private Button approveBtn;
    @FXML private Button deleteBtn;
    @FXML private Button backBtn;

    private ObservableList<StockRequest> getRequestData() {
        ObservableList<StockRequest> data = FXCollections.observableArrayList();
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            StockRequestDAO dao = new StockRequestDAO(conn);
            data.addAll(dao.getAllRequests());
            System.out.println("Loaded requests: " + data.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Requests Found");
            alert.setHeaderText(null);
            alert.setContentText("No stock requests found in the database.");
            alert.showAndWait();
        }
        return data;
    }

    @FXML
    private void initialize() {
        productColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProductName()));
        quantityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        dateColumn.setCellValueFactory(cellData -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(cellData.getValue().getDateReq()));
        });
        userNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(getUserName(cellData.getValue().getUserId())));
        supplierNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(getSupplierName(cellData.getValue().getSupplierId())));
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        ObservableList<StockRequest> requests = getRequestData();
        System.out.println("Table will show " + requests.size() + " requests.");
        requestsTable.setItems(requests);
        addRequestBtn.setOnAction(this::handleAddRequest);
        approveBtn.setOnAction(this::handleApproveRequest);
        deleteBtn.setOnAction(this::handleDeleteRequest);
        backBtn.setOnAction(this::handleBack);

    }

    private String getUserName(long userId) {
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            com.ism.dao.UserDAO dao = new com.ism.dao.UserDAO(conn);
            // Assuming a method getUserById exists
            com.ism.models.User user = dao.getUserById(userId);
            if (user != null) return user.getFirstName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User" + userId;
    }

    private String getSupplierName(long supplierId) {
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            com.ism.dao.SupplierDAO dao = new com.ism.dao.SupplierDAO(conn);
            // Assuming a method getSupplierById exists
            com.ism.models.Supplier supplier = dao.getSupplierById(supplierId);
            if (supplier != null) return supplier.getSupplierFname();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Supplier" + supplierId;
    }

    @FXML
    private void handleAddRequest(ActionEvent event) {
        StockRequestFormDialog dialog = new StockRequestFormDialog();
        dialog.showAndWait().ifPresent(request -> {
            try (java.sql.Connection conn = com.ism.utils.DBConnect.getConnection()) {
                com.ism.dao.StockRequestDAO dao = new com.ism.dao.StockRequestDAO(conn);
                dao.addStockRequest(request);
                requestsTable.setItems(getRequestData());
                requestsTable.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Failed to add stock request. Please check your input and try again.");
            }
        });
    }

    @FXML
    private void handleApproveRequest(ActionEvent event) {
        StockRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a request to approve.");
            return;
        }
        try (java.sql.Connection conn = DBConnect.getConnection()) {
            StockRequestDAO dao = new StockRequestDAO(conn);
            dao.approveRequest(selected.getRequestId());
            requestsTable.setItems(getRequestData());
            requestsTable.refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Failed to approve request. Please try again.");
        }
    }

    @FXML
    private void handleDeleteRequest(ActionEvent event) {
        StockRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a request to delete.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Request");
        alert.setHeaderText("Are you sure you want to delete this request?");
        alert.setContentText(selected.getProductName());
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try (java.sql.Connection conn = DBConnect.getConnection()) {
                    StockRequestDAO dao = new StockRequestDAO(conn);
                    dao.deleteRequest(selected.getRequestId());
                    requestsTable.setItems(getRequestData());
                    requestsTable.refresh();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Failed to delete request. Please try again.");
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
