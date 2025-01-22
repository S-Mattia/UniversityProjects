module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires SharedModel;
    requires java.compiler;
    requires java.desktop;


    opens com.example.client to javafx.fxml;
    exports com.example.client;
    exports com.example.client.support;
    opens com.example.client.support to javafx.fxml;

}