package be.helha.java24groupe02.client.views;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import be.helha.java24groupe02.client.views.TemplateViewSnackOrderSummary.QuantityChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.List;

import javafx.util.Pair;

/**
 * The controller for the snack view.
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

    @FXML
    private Button confirmOrderButton;

    private List<Product> products;
    private Product selectedProduct;
    private Cart cart;
    private boolean dataInitialized = false;

    private QuantityChangeListener quantityChangeListener;
    private CartListener cartListener;

    private TemplateViewSnackOrderSummary templateViewSnackOrderSummary;

    public void setTemplateViewSnack(TemplateViewSnackOrderSummary templateViewSnackOrderSummary) {
        this.templateViewSnackOrderSummary = templateViewSnackOrderSummary;
    }

    public void setCartListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

    public void setQuantityChangeListener(TemplateViewSnackOrderSummary.QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    /**
     * Initialize the view.
     */
    @FXML
    public void initialize() {
        if (!dataInitialized && products != null && cart != null) {
            initializeView();
            dataInitialized = true;
        }
    }

    /**
     * Load the snack template view controller.
     *
     * @param productInCart the product in the cart
     * @return the root and controller pair
     */
    private Pair<Parent, TemplateViewSnackOrderSummary> loadTemplateViewSnackController(Product productInCart) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnackOrderSummary.fxml"));
            Parent root = loader.load();
            TemplateViewSnackOrderSummary controller = loader.getController();
            controller.setSnackViewController(this);
            controller.setUniqueId(productInCart.getProductId());
            return new Pair<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add a snack to the order summary.
     */
    private void addSnackToOrderSummary(Product productInCart) {
            Pair<Parent, TemplateViewSnackOrderSummary> pair = loadTemplateViewSnackController(productInCart);
            Parent root = pair.getKey();
            TemplateViewSnackOrderSummary controller = pair.getValue();
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
     * Add a snack to the interface.
     *
     * @param products the snack to add
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
     * Manage the click on a snack button.
     *
     * @param products the snack clicked
     */
    private void handleSnackButtonClick(Product products) {
        selectedProduct = products;
        updateProductButtonAppearance();
    }

    /**
     * Update the appearance of the product buttons.
     */
    private void updateProductButtonAppearance() {
        // Go through all the buttons in the flow pane
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button button) {
                Product products = getProductIdFromButton(button);

                // Check if the button corresponds to the selected snack
                if (selectedProduct == products) {
                    // Change the style of the button
                    button.setStyle("-fx-background-color: lightblue;");
                } else {
                    // Reset the style of the button
                    button.setStyle("");
                }
            }
        }
    }

    /**
     * Get the snack corresponding to the button.
     *
     * @param button the button to get the snack from
     * @return the snack corresponding to the button if found, null otherwise
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
     * Update the order.
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
                templateViewSnackOrderSummary.snackQuantityVisual(Integer.toString(selectedProduct.getProductId()) ,selectedProductQuantity, selectedProduct);
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

    /**
     * Search for a product in the cart.
     *
     * @param productId the product ID to search for
     * @return the product if found, null otherwise
     */
    private Product findProductInCart(int productId) {
        for (Product product : cart.getCartItems()) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Update the cart total.
     */
    public void updateCartTotal(Double totalPrice) {
        totalPriceLabel.setText(totalPrice + "€");
    }

    public void removeProductFromOrderSummary(int productId) {
        System.out.println("Removing product from order summary");
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

    /**
     * Initialize the data for the view.
     *
     * @param products the products
     * @param cart     the cart
     */
    public void initData(List<Product> products, Cart cart) {
        this.products = products;
        this.cart = cart;
        if (!dataInitialized && products != null && cart != null) {
            initializeView();
            dataInitialized = true;
        }
    }

    /**
     * Confirm the order.
     */
    private void confirmOrder() {
        cartListener.confirmOrder();
    }

    private void initializeView() {
        for (Product product : products) {
            addSnackToInterface(product);
        }
        addSnackToOrderButton.setOnAction(event -> updateOrder());
        confirmOrderButton.setOnAction(event -> confirmOrder());
    }

    public void updateProducts(List<Product> updatedProducts) {
        products.clear();
        products.addAll(updatedProducts);
        viewSnacksFlowPane.getChildren().clear();
        for (Product product : products) {
            addSnackToInterface(product);
        }
    }

    public void orderSummaryCreated() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Commande confirmée");
        alert.setHeaderText("Votre commande a été confirmée.");
        alert.setContentText("Merci pour votre commande !");
        alert.showAndWait();
    }



    public interface CartListener {
        void onProductAddedToCart(Product product);
        void confirmOrder();
        void addSnackQuantity(Product product) throws NoMoreStockException;
    }

    public ObservableList<Node> getViewOrderVBoxChildren() {
        return viewOrderVBox.getChildren();
    }

}