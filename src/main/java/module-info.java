module org.example.java24groupe02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;

    opens be.helha.java24groupe02 to javafx.fxml;
    exports be.helha.java24groupe02;
}