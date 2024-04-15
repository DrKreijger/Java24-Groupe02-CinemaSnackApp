package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    public void getSelectedProductData (Product selectedProduct) {
        // Charger l'image du snack
        Image productImage = new Image("file:" + selectedProduct.getImagePath());
        ImageSnackCart.setImage(productImage);
        NameSnackCart.setText(selectedProduct.getName());
        FlavorSnackCart.setText(selectedProduct.getFlavor());
        SizeSnackCart.setText(selectedProduct.getSize());
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice()));
    }
}
