module be.helha.java24groupe02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;

    exports be.helha.java24groupe02.views;
    opens be.helha.java24groupe02.views to javafx.fxml;
    exports be.helha.java24groupe02.controllers;
    exports be.helha.java24groupe02.server.models;
    opens be.helha.java24groupe02.server.models to javafx.fxml, com.google.gson;
}