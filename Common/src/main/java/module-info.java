module Common {
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;
    requires javafx.controls;

    exports be.helha.java24groupe02.models;
    exports be.helha.java24groupe02.models.exceptions;

    opens be.helha.java24groupe02.models to javafx.fxml, com.google.gson;
}