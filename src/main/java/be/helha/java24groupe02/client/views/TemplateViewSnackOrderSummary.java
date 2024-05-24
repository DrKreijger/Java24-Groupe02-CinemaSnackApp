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

/**
 * This class represents a template view for a snack order summary in a JavaFX application.
 * It manages the display of product data in the user interface and the interaction with the cart.
 */
public class TemplateViewSnackOrderSummary {

    // JavaFX UI components
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

    // The controller of the snack view
    private SnackViewController snackViewController;

    // The unique id of the snack
    private int uniqueId;

    // Listener for changes in the quantity of the snack
    private QuantityChangeListener quantityChangeListener;

    /**
     * Initializes the view. This method is called after all @FXML annotated members have been injected.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Sets the product data to be displayed in the view.
     *
     * @param selectedProduct the product whose data is to be displayed
     */
    public void getSelectedProductData (Product selectedProduct) {
        // Load the image of the snack
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

    /**
     * Handles the event of adding quantity to a snack.
     *
     * @param selectedProduct the product to add quantity to
     * @throws NoMoreStockException if the product does not have stock
     */
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

    /**
     * Handles the event of removing quantity from a snack.
     *
     * @param selectedProduct the product to remove quantity from
     */
    public void handleRemoveSnackQuantity(Product selectedProduct) {
        int quantity = selectedProduct.getQuantity();
        quantity--;
        snackQuantityVisual(quantity, selectedProduct);

        if (quantityChangeListener != null) {
            quantityChangeListener.removeSnackQuantity(selectedProduct);
        }
    }

    /**
     * Updates the visual representation of the snack quantity in the view.
     *
     * @param uniqueId the unique id of the snack
     * @param quantity the new quantity of the snack
     * @param selectedProduct the product to update the quantity of
     */
    public void snackQuantityVisual(String uniqueId, int quantity, Product selectedProduct) {
        // Get the children of viewOrderVBox from SnackViewController
        ObservableList<Node> children = snackViewController.getViewOrderVBoxChildren();

        // Check if the list of children is not empty
        if (children != null && !children.isEmpty()) {
            // Iterate over the children to find the TemplateViewSnackOrderSummary with the unique id
            for (Node node : children) {
                if (node instanceof Parent root && node.getId() != null && node.getId().equals(uniqueId)) {
                    // Identify the root node of the TemplateViewSnackOrderSummary
                    // Access the label inside the TemplateViewSnackOrderSummary using a CSS selector
                    Label quantityLabel = (Label) root.lookup("#QuantitySnackCart");
                    Label priceLabel = (Label) root.lookup("#PriceSnackCart");
                    if (quantityLabel != null) {
                        // Update the text of the label with the new quantity
                        quantityLabel.setText(String.valueOf(quantity));
                        priceLabel.setText(String.valueOf(quantity * selectedProduct.getPrice()));
                    }
                    // Exit the loop once the appropriate TemplateViewSnackOrderSummary is found
                    break;
                }
            }
        }
    }

    /**
     * Updates the visual representation of the snack quantity in the view.
     *
     * @param quantity the new quantity of the snack
     * @param selectedProduct the product to update the quantity of
     */
    public void snackQuantityVisual(int quantity, Product selectedProduct) {
        QuantitySnackCart.setText(String.valueOf(quantity));
        PriceSnackCart.setText(String.valueOf(selectedProduct.getPrice() * quantity));
    }

    /**
     * Handles the event of deleting a snack from the cart.
     *
     * @param selectedProduct the product to delete
     */
    public void handleDeleteSnackCart(Product selectedProduct) {
        quantityChangeListener.deleteSnack(selectedProduct);
    }

    /**
     * Sets the snack view controller.
     *
     * @param snackViewController the snack view controller to set
     */
    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    /**
     * Sets the unique id of the snack.
     *
     * @param uniqueId the unique id to set
     */
    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Interface for a quantity change listener.
     */
    public interface QuantityChangeListener {
        void deleteSnack(Product product);
        void addSnackQuantity(Product product) throws NoMoreStockException;
        void removeSnackQuantity(Product product);
    }

    /**
     * Sets the quantity change listener.
     *
     * @param listener the quantity change listener to set
     */
    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

}
