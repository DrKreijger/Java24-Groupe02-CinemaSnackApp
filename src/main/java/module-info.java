module be.helha.java24groupe02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    exports be.helha.java24groupe02.views;
    opens be.helha.java24groupe02.views to javafx.fxml;
    exports be.helha.java24groupe02.controllers;
    exports be.helha.java24groupe02.models;
}