module com.ism {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.ism;
    exports com.ism.controllers to javafx.fxml;
    exports com.ism.dao;
    exports com.ism.models;
    exports com.ism.utils;

    opens com.ism.controllers to javafx.fxml;
}
