package be.helha.java24groupe02.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class TemplateViewSnack {

    private int quantity = 1;

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
    private Label QuantitySnackCart;

    @FXML
    private Button removeSnackFromOrderButton;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Button DeleteSnackCart;

    public void initialize() {
        addSnackToOrderButton.setOnAction(event -> {
            quantity++;
            QuantitySnackCart.setText(String.valueOf(quantity));
        });

        removeSnackFromOrderButton.setOnAction(event -> {
            if (quantity > 1) {
                quantity--;
                QuantitySnackCart.setText(String.valueOf(quantity));
            }
        });
        DeleteSnackCart.setOnAction(event -> handleDeleteButtonClick());
    }

    private void handleDeleteButtonClick() {
        NameSnackCart.setText("");
        FlavorSnackCart.setText("");
        SizeSnackCart.setText("");
        PriceSnackCart.setText("");
        ImageSnackCart.setImage(null);
        QuantitySnackCart.setText("");
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
}
