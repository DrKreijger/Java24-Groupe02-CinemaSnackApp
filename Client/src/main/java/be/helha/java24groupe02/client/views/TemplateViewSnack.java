package be.helha.java24groupe02.client.views;

import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;

public class TemplateViewSnack {
    @FXML
    private AnchorPane AnchorPaneSnackOrderSummary;

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

    private int uniqueId;


    @FXML
    public void initialize() {
    }

    private QuantityChangeListener quantityChangeListener;


    public void getSelectedProductData (Product selectedProduct) {
        // Charger l'image du snack
        URL selectedProductImageURL = (selectedProduct.getImagePath());
        Image productImage = new Image(selectedProductImageURL.toExternalForm());
        ImageSnackCart.setImage(productImage);
        NameSnackCart.setText(selectedProduct.getName());
        FlavorSnackCart.setText(selectedProduct.getFlavor());
        SizeSnackCart.setText(selectedProduct.getSize());
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice()));
        QuantitySnackCart.setText(String.valueOf(selectedProduct.getQuantity()));
        AnchorPaneSnackOrderSummary.setId(String.valueOf(selectedProduct.getProductId()));
    }

    public void handleAddSnackQuantity(Product selectedProduct) throws NoMoreStockException {
        try {
            if (quantityChangeListener != null) {
                quantityChangeListener.addSnackQuantity(selectedProduct);
            }
            int quantity = selectedProduct.getQuantity();
            snackQuantityVisual(quantity, selectedProduct);
        } catch (NoMoreStockException e) {
            e.showError();
        }
    }


    public void handleRemoveSnackQuantity(Product selectedProduct) {
        int quantity = selectedProduct.getQuantity();
        quantity--;
        snackQuantityVisual(quantity, selectedProduct);

        if (quantityChangeListener != null) {
            quantityChangeListener.removeSnackQuantity(selectedProduct);
        }
    }

    
    public void snackQuantityVisual(String uniqueId, int quantity, Product selectedProduct) {
        // Obtenir les enfants de viewOrderVBox depuis SnackViewController
        ObservableList<Node> children = snackViewController.getViewOrderVBoxChildren();

        // Vérifier si la liste des enfants n'est pas vide
        if (children != null && !children.isEmpty()) {
            // Parcourir les enfants pour trouver le TemplateViewSnack avec l'identifiant unique
            for (Node node : children) {
                if (node instanceof Parent root && node.getId() != null && node.getId().equals(uniqueId)) {
                    // Identifier le nœud racine du TemplateViewSnack
                    // Accéder au label à l'intérieur du TemplateViewSnack en utilisant un sélecteur CSS
                    Label quantityLabel = (Label) root.lookup("#QuantitySnackCart");
                    Label priceLabel = (Label) root.lookup("#PriceSnackCart");
                    if (quantityLabel != null) {
                        // Modifier le texte du label avec la nouvelle quantité
                        quantityLabel.setText(String.valueOf(quantity));
                        priceLabel.setText(String.valueOf(quantity * selectedProduct.getPrice()));
                    }
                    // Sortir de la boucle une fois que le TemplateViewSnack approprié est trouvé
                    break;
                }
            }
        }
    }


    public void snackQuantityVisual(int quantity, Product selectedProduct) {
        QuantitySnackCart.setText(String.valueOf(quantity));
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice() * quantity));
    }

    public void handleDeleteSnackCart(Product selectedProduct) {
        quantityChangeListener.deleteSnack(selectedProduct);
    }

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public interface QuantityChangeListener {
        void deleteSnack(Product product);
        void addSnackQuantity(Product product) throws NoMoreStockException;
        void removeSnackQuantity(Product product);
    }

    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

}
