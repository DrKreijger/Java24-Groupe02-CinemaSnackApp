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
    private SnackViewController snackViewController;


    @FXML
    public void initialize() {
    }

    private QuantityChangeListener quantityChangeListener;


    public void getSelectedProductData (Product selectedProduct) {
        // Charger l'image du snack
        Image productImage = new Image("file:" + selectedProduct.getImagePath());
        ImageSnackCart.setImage(productImage);
        NameSnackCart.setText(selectedProduct.getName());
        FlavorSnackCart.setText(selectedProduct.getFlavor());
        SizeSnackCart.setText(selectedProduct.getSize());
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice()));
        QuantitySnackCart.setText(String.valueOf(selectedProduct.getQuantity()));
    }

    public void handleAddSnackQuantity(Product selectedProduct) {
        int quantity = Integer.parseInt(QuantitySnackCart.getText());
        quantity++;
        selectedProduct.setQuantity(quantity);
        QuantitySnackCart.setText(String.valueOf(quantity));

        if (quantityChangeListener != null) {
            quantityChangeListener.onQuantityChanged(selectedProduct, quantity);
        }
    }

    public void handleRemoveSnackQuantity(Product selectedProduct) {
        int quantity = Integer.parseInt(QuantitySnackCart.getText());
        if (quantity > 1) {
            quantity--;
            selectedProduct.setQuantity(quantity);
            QuantitySnackCart.setText(String.valueOf(quantity));

            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(selectedProduct, quantity);
            }
        }
    }

    public void handleDeleteSnackCart(Product selectedProduct) {
            quantityChangeListener.onQuantityChanged(selectedProduct, 0);
    }

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    public SnackViewController getSnackViewController() {
        return snackViewController;
    }

    public interface QuantityChangeListener {
        void onQuantityChanged(Product product, int quantity);
    }

    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }
}
