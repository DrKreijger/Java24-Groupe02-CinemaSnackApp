package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.List;

/**
 * Contrôleur de vue pour la gestion des snacks.
 */
public class SnackViewController {

    @FXML
    private FlowPane viewSnacksFlowPane;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private VBox viewOrderVBox;

    private List<Product> products;

    private SnackViewListener listener;

    private Product selectedProduct;

    private Cart cart;

    public void setListener(SnackViewListener listener) {
        this.listener = listener;
    }
//    ProductDB productDB = new ProductDB();
//    private List<Product> products = productDB.getAllProductsFromDatabase();
//    private Product selectedProduct;
//    private Cart cart = new Cart();


    /**
     * Initialise le contrôleur de vue. Charge les produits depuis la base de données et les ajoute à l'interface.
     */
    @FXML
    public void initialize() {
        for (Product product : this.products) {
            addSnackToInterface(product);
        }
        addSnackToOrderButton.setOnAction(event -> updateOrder());
    }

    /**
     * Ajoute un snack à la commande au résumé de la commande.
     */
    private void addSnackToOrderSummary() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnack.fxml"));
        try {
            Parent root = loader.load();
            TemplateViewSnack controller = loader.getController();
            controller.getSelectedProductData(selectedProduct);
            viewOrderVBox.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ajoute un snack à l'interface utilisateur.
     *
     * @param products le snack à ajouter
     */
    private void addSnackToInterface(Product products) {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewButtonSnack.fxml"));
        try{
            Button snackButton = loader.load();
            TemplateViewButtonSnack controller = loader.getController();

            controller.setProductData(products);

            snackButton.setOnAction(event -> handleSnackButtonClick(products));

            viewSnacksFlowPane.getChildren().add(snackButton);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Gère le clic sur un bouton de snack.
     *
     * @param products le snack associé au bouton cliqué
     */
    private void handleSnackButtonClick(Product products) {
        setSelectedProduct(products);
        updateProductButtonAppearance();
    }

    /**
     * Met à jour l'apparence des boutons de snacks.
     */
    private void updateProductButtonAppearance() {
        // Parcourir tous les boutons de snacks
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Product products = getProductIdFromButton(button);

                // Vérifier si le snack est sélectionné
                if (selectedProduct == products) {
                    // Mettre à jour l'apparence pour le snack sélectionné
                    button.setStyle("-fx-background-color: lightblue;");
                    System.out.println("Snack sélectionné : " + products.getId());
                } else {
                    // Mettre à jour l'apparence pour le snack non sélectionné
                    button.setStyle(""); // Reset style to default
                }
            }
        }
    }

    /**
     * Récupère le snack associé à un bouton.
     *
     * @param button le bouton associé au snack
     * @return le snack correspondant ou null si aucun snack correspondant n'est trouvé
     */
    private Product getProductIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Product products : this.products) {
            if ((String.valueOf(products.getId()).equals(buttonId))) {
                return products;
            }
        }
        return null; // Si aucun snack correspondant n'est trouvé
    }

    /**
     * Met à jour la commande.
     */
    private void updateOrder() {
        if (selectedProduct != null) {
            cart.addProductToCart(selectedProduct);
            updateCartTotal();
            addSnackToOrderSummary();
        }
    }
    /**
     * Met à jour le prix total du panier.
     */
    private void updateCartTotal() {
        totalPriceLabel.setText(cart.getTotalPrice() + "€");
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setSelectedProduct(Product products) {
        this.selectedProduct = products;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public interface SnackViewListener {
        void handleSnackButtonClick(Product products);
    }
}