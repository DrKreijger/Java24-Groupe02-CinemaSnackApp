package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnackViewController {

    @FXML
    private FlowPane viewSnacksFlowPane;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private VBox viewOrderVBox;

    ProductDB productDB = new ProductDB();
    private List<Product> products = productDB.getAllProductsFromDatabase();
    private Product selectedProduct;
    private Cart cart = new Cart();

    private List<Product> cartItems = new ArrayList<>();

    @FXML
    public void initialize() {
        for (Product product : this.products) {
            addSnackToInterface(product);
        }
        addSnackToOrderButton.setOnAction(event -> updateOrder());
    }

    private void addSnackToInterface(Product product) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewButtonSnack.fxml"));
        try {
            Button snackButton = loader.load();
            TemplateViewButtonSnack controller = loader.getController();
            controller.setProductData(product);
            snackButton.setOnAction(event -> handleSnackButtonClick(product));
            viewSnacksFlowPane.getChildren().add(snackButton);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSnackButtonClick(Product product) {
        selectedProduct = product;
        updateProductButtonAppearance();
    }

    private void updateProductButtonAppearance() {
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Product product = getProductIdFromButton(button);
                if (selectedProduct == product) {
                    button.setStyle("-fx-background-color: lightblue;");
                    System.out.println("Snack sélectionné : " + product.getId());
                } else {
                    button.setStyle("");
                }
            }
        }
    }

    private Product getProductIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Product product : this.products) {
            if (String.valueOf(product.getId()).equals(buttonId)) {
                return product;
            }
        }
        return null;
    }

    private void updateOrder() {
        if (selectedProduct != null) {
            cart.addProductToCart(selectedProduct);
            updateCartTotal();
            addSnackToOrderSummary();
        }
    }

    private void addSnackToOrderSummary() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnack.fxml"));
        try {
            Parent root = loader.load();
            TemplateViewSnack controller = loader.getController();
            controller.setSnackViewController(this);
            controller.getSelectedProductData(selectedProduct);
            viewOrderVBox.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateCartTotal() {
        double totalPrice = cart.getTotalPrice(); // Accès à l'attribut totalPrice de l'objet cart
        totalPriceLabel.setText(totalPrice + "€");
    }

    protected void deleteSnackFromCart(int productId) {
        // Supprimer le produit correspondant du panier
        cart.removeProductFromCart(productId);
        // Mettre à jour l'affichage du panier
        updateCartTotal();
        // Mettre à jour la vue de la commande
        updateCartView();
    }

    protected void updateCartView() {
        viewOrderVBox.getChildren().clear();
        for (Product product : cartItems) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnack.fxml"));
            try {
                Parent root = loader.load();
                TemplateViewSnack controller = loader.getController();
                controller.setSnackViewController(this);
                controller.getSelectedProductData(product);
                viewOrderVBox.getChildren().add(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
