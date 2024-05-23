module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires Common;

    exports be.helha.java24groupe02.client.views;
    opens be.helha.java24groupe02.client.views to javafx.fxml;
    exports be.helha.java24groupe02.client.controllers;

}