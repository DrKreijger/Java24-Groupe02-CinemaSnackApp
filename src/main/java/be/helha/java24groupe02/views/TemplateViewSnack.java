package be.helha.java24groupe02.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TemplateViewSnack {

    @FXML
    private Label NameSnackCart;


    @FXML
    private Label FlavorSnackCart;

    public Label getNameSnackCart() {
        return NameSnackCart;
    }
    public Label getFlavorSnackCart() {
        return FlavorSnackCart;
    }
}
