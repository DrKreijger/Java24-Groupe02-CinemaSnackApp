module be.helha.java24groupe02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;

    exports be.helha.java24groupe02.view;
    opens be.helha.java24groupe02.view to javafx.fxml;
    exports be.helha.java24groupe02.controller;
    exports be.helha.java24groupe02.model;
}