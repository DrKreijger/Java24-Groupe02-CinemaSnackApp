package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import javafx.collections.ObservableList;
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
import be.helha.java24groupe02.views.TemplateViewSnack.QuantityChangeListener;
import javafx.util.Pair;

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

    ProductDB productDB;
    private List<Product> products;
    private Product selectedProduct;
    private Cart cart;
    private boolean dataInitialized = false;

    private QuantityChangeListener quantityChangeListener;
    private CartListener cartListener;

    private TemplateViewSnack templateViewSnack;

    public void setTemplateViewSnack(TemplateViewSnack templateViewSnack) {
        this.templateViewSnack = templateViewSnack;
    }

    public void setCartListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

    public void setQuantityChangeListener(TemplateViewSnack.QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    /**
     * Initialise le contrôleur de vue. Charge les produits depuis la base de données et les ajoute à l'interface.
     */
    @FXML
    public void initialize() {
        if (!dataInitialized && productDB != null && products != null && cart != null) {
            initializeView();
            dataInitialized = true;
        }
    }

    private Pair<Parent, TemplateViewSnack> loadTemplateViewSnackController(Product productInCart) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnack.fxml"));
            Parent root = loader.load();
            TemplateViewSnack controller = loader.getController();
            controller.setSnackViewController(this);
            controller.setUniqueId(productInCart.getProductId());
            return new Pair<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ajoute un snack à la commande au résumé de la commande.
     */
    private void addSnackToOrderSummary(Product productInCart) {
            Pair<Parent, TemplateViewSnack> pair = loadTemplateViewSnackController(productInCart);
            Parent root = pair.getKey();
            TemplateViewSnack controller = pair.getValue();
            setTemplateViewSnack(controller);
            controller.getSelectedProductData(selectedProduct);
            controller.setQuantityChangeListener(quantityChangeListener);
            controller.addSnackQuantityButton.setOnAction(event -> {
                try {
                    controller.handleAddSnackQuantity(productInCart);
                } catch (NoMoreStockException e) {
                    throw new RuntimeException(e);
                }
            });
            controller.removeSnackQuantityButton.setOnAction(event -> controller.handleRemoveSnackQuantity(productInCart));
            controller.DeleteSnackCart.setOnAction(event -> controller.handleDeleteSnackCart(productInCart));
            controller.setQuantityChangeListener(quantityChangeListener);

            viewOrderVBox.getChildren().add(root);
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
        selectedProduct = products;
        updateProductButtonAppearance();
    }

    /**
     * Met à jour l'apparence des boutons de snacks.
     */
    private void updateProductButtonAppearance() {
        // Parcourir tous les boutons de snacks
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button button) {
                Product products = getProductIdFromButton(button);

                // Vérifier si le snack est sélectionné
                if (selectedProduct == products) {
                    // Mettre à jour l'apparence pour le snack sélectionné
                    button.setStyle("-fx-background-color: lightblue;");
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
            if ((String.valueOf(products.getProductId()).equals(buttonId))) {
                return products;
            }
        }
        return null; // Si aucun snack correspondant n'est trouvé
    }

    /**
     * Met à jour la commande.
     */
    private void updateOrder() {
        if (selectedProduct != null && cartListener != null) {
            int selectedProductQuantity = selectedProduct.getQuantity();
            selectedProductQuantity++;
            // Ajouter le produit au panier
            Product productInCart = findProductInCart(selectedProduct.getProductId());
            if (productInCart != null) {
                try {
                // Le produit est déjà dans le panier, aucune action supplémentaire requise
                cartListener.addSnackQuantity(selectedProduct);
                templateViewSnack.snackQuantityVisual(Integer.toString(selectedProduct.getProductId()) ,selectedProductQuantity, selectedProduct);
                } catch (NoMoreStockException e) {
                    e.showError();
                }
            } else {
                cartListener.onProductAddedToCart(selectedProduct);
                addSnackToOrderSummary(selectedProduct);
            }
        }
        updateCartTotal(cart.getTotalPrice());
    }


    private Product findProductInCart(int productId) {
        for (Product product : cart.getCartItems()) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Met à jour le prix total du panier.
     */
    public void updateCartTotal(Double totalPrice) {
        totalPriceLabel.setText(totalPrice + "€");
    }

    public void removeProductFromOrderSummary(int productId) {
        for (Node node : viewOrderVBox.getChildren()) {
            if (node instanceof Parent parent) {
                String id = parent.getId();
                if (id.equals(String.valueOf(productId))) {
                    viewOrderVBox.getChildren().remove(parent);
                    break;
                }
            }
        }
    }

    public void initData(ProductDB productDB, List<Product> products, Cart cart) {
        this.productDB = productDB;
        this.products = products;
        this.cart = cart;
        if (!dataInitialized && productDB != null && products != null && cart != null) {
            initializeView();
            dataInitialized = true;
        }
    }

    private void initializeView() {
        // Initialiser l'interface utilisateur avec les données
        for (Product product : products) {
            addSnackToInterface(product);
        }
        // Définir les actions des boutons
        addSnackToOrderButton.setOnAction(event -> updateOrder());
    }

    public interface CartListener {
        void onProductAddedToCart(Product product);
        void addSnackQuantity(Product product) throws NoMoreStockException;
    }

    public ObservableList<Node> getViewOrderVBoxChildren() {
        return viewOrderVBox.getChildren();
    }

}