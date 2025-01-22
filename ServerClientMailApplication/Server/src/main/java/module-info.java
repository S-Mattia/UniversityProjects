module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires SharedModel;


    opens com.example.server to javafx.fxml;
    exports com.example.server;
    exports com.example.server.support;
    opens com.example.server.support to javafx.fxml;
}