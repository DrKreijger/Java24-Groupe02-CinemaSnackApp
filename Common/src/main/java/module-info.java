module Common {
    requires java.sql;
    requires com.google.gson;
    requires org.mockito;
    requires javafx.controls;

    exports be.helha.java24groupe02.models;
    exports be.helha.java24groupe02.models.exceptions;

    opens be.helha.java24groupe02.models to javafx.fxml, com.google.gson;
}