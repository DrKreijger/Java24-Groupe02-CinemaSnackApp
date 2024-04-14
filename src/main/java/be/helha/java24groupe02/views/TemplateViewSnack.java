package be.helha.java24groupe02.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class TemplateViewSnack {
    @FXML
    private Label NameSnackCart;

    @FXML
    private Label FlavorSnackCart;

    @FXML
    private Label SizeSnackCart;

    @FXML
    private Label PriceSnackCart;

    @FXML
    private ImageView ImageSnackCart;

    @FXML
    public Label QuantitySnackCart;

    @FXML
    public Button removeSnackQuantityButton;

    @FXML
    public Button addSnackQuantityButton;

    @FXML
    public Button DeleteSnackCart;

    public void initialize() {
    }

    public Label getNameSnackCart() {
        return NameSnackCart;
    }
    public Label getFlavorSnackCart() {
        return FlavorSnackCart;
    }
    public Label getSizeSnackCart() {
        return SizeSnackCart;
    }
    public Label getPriceSnackCart() {
        return PriceSnackCart;
    }
    public ImageView getImageSnackCart() {
        return ImageSnackCart;
    }
    public Label getQuantitySnackCart() {
        return QuantitySnackCart;
    }
    public void addQuantitySnackCart() {
        QuantitySnackCart.setText(String.valueOf(Integer.parseInt(QuantitySnackCart.getText()) + 1));
    }
}
