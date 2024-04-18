package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private Button DeleteSnackCart;

    private SnackViewController snackViewController;

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    @FXML
    public void initialize() {
        DeleteSnackCart.setOnAction(event -> handleDeleteButtonClick(event));
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

    public void getSelectedProductData(Product selectedProduct) {
        Image productImage = new Image("file:" + selectedProduct.getImagePath());
        ImageSnackCart.setImage(productImage);
        NameSnackCart.setText(selectedProduct.getName());
        FlavorSnackCart.setText(selectedProduct.getFlavor());
        SizeSnackCart.setText(selectedProduct.getSize());
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice()));
    }

    public void handleDeleteButtonClick(ActionEvent event) {
        // Récupérer l'AnchorPane parent du bouton DeleteSnackCart
        AnchorPane parentPane = (AnchorPane) DeleteSnackCart.getParent();
        if (parentPane != null) {
            // Supprimer l'AnchorPane parent
            parentPane.getChildren().clear(); // Supprimer tous les enfants de l'AnchorPane
            // Mettre à jour le panier dans le contrôleur SnackViewController
            Product product = getProductFromTemplateView();
            snackViewController.deleteSnackFromCart(product.getId()); // Appeler la méthode deleteSnackFromCart
        } else {
            System.err.println("Impossible de trouver le parent de DeleteSnackCart.");
        }
    }

    public Product getProductFromTemplateView() {
        Product product = new Product();
        product.setId(Integer.parseInt(DeleteSnackCart.getId())); // Utiliser l'ID du bouton
        product.setName(NameSnackCart.getText());
        product.setFlavor(FlavorSnackCart.getText());
        product.setSize(SizeSnackCart.getText());
        product.setPrice(Double.parseDouble(PriceSnackCart.getText()));
        product.setImagePath(ImageSnackCart.getImage().getUrl());
        return product;
    }

}