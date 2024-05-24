package be.helha.java24groupe02.client.controllers;

import be.helha.java24groupe02.client.network.SnackClient;
import be.helha.java24groupe02.client.views.SnackViewController;
import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.CartObserver;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import be.helha.java24groupe02.client.views.TemplateViewSnackOrderSummary.QuantityChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


/**
 * MainController is the main class of the application. It extends the Application class from JavaFX,
 * and implements the CartListener, QuantityChangeListener, and CartObserver interfaces.
 * It manages the main operations of the application, such as initializing the cart, the network client,
 * and the view, as well as updating the products from the server.
 */
public class MainController extends Application implements SnackViewController.CartListener, QuantityChangeListener, CartObserver {

    // The cart of the application
    private Cart cart;
    // The controller of the snack view
    private SnackViewController snackViewController;
    // The client for the snack network
    private SnackClient snackClient;

    /**
     * Sets the snack view controller.
     *
     * @param snackViewController the snack view controller to set
     */
    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    /**
     * Starts the application. Initializes the cart and the network client.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
            try {
                initializeCart();
                initializeNetworkClient();
            } catch (Exception e) {
                Platform.exit();
            }
    }

    /**
     * The main entry point for all JavaFX applications.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Updates the products from the server. If the snack view controller is not initialized, it initializes the view.
     *
     * @param updatedProducts the updated products from the server
     */
    private void updateProductsFromServer(List<Product> updatedProducts) {
        Platform.runLater(() -> {
            if (snackViewController == null) {
                try {
                    initializeView(updatedProducts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                snackViewController.updateProducts(updatedProducts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initializes the view. Loads the FXML file, initializes the controller, and shows the stage.
     *
     * @param products the products to initialize the view with
     * @throws IOException if an I/O error occurs
     */
    private void initializeView(List<Product> products) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
        Parent root = fxmlLoader.load();
        initializeController(products, fxmlLoader);
        initializeStage(root);
    }

    /**
     * Initializes the stage. Sets the title, the scene, and the close request handler, and shows the stage.
     *
     * @param root the root node of the scene
     */
    private void initializeStage(Parent root) {
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Snacks App");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            snackClient.stop();
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    /**
     * Initializes the controller. Sets the data, the cart listener, the quantity change listener, and the snack view controller.
     *
     * @param products the products to initialize the controller with
     * @param fxmlLoader the FXMLLoader to get the controller from
     */
    private void initializeController(List<Product> products, FXMLLoader fxmlLoader) {
        SnackViewController controller = fxmlLoader.getController();
        controller.initData(products, cart);
        controller.setCartListener(this);
        controller.setQuantityChangeListener(this);
        setSnackViewController(controller);
    }

    /**
     * Initializes the network client. Starts the client and sets the products update listener.
     */
    private void initializeNetworkClient() {
        snackClient = new SnackClient();
        new Thread(() -> {
            snackClient.start();
            snackClient.setProductsUpdateListener(this::updateProductsFromServer);
        }).start();
    }

    /**
     * Initializes the cart. Adds this class as an observer of the cart.
     */
    private void initializeCart() {
        cart = new Cart();
        cart.addObserver(this);
    }

    /**
     * Handles the event of a product being added to the cart. Removes stock from the product, adds the product to the cart,
     * and prints the quantity in stock of the product.
     *
     * @param product the product that was added to the cart
     */
    @Override
    public void onProductAddedToCart(Product product) {
            product.removeStock();
            int productId = product.getProductId();
            int clientStock = product.getQuantityInStock();
            snackClient.addToCart(productId, clientStock);
            cart.addProductToCart(product);
            System.out.println("Product quantity in stock: " + product.getQuantityInStock());
    }

    /**
     * Deletes a snack from the cart. Updates the quantity in stock of the product, removes the product from the order summary,
     * and prints the quantity in the cart and in stock of the product, as well as the products in the cart.
     *
     * @param product the product to delete
     */
    @Override
    public void deleteSnack(Product product) {
        int productId = product.getProductId();
        int currentQuantityInStock = product.getQuantityInStock();
        int quantityRemoved = product.getQuantity();
        int newQuantityInStock = currentQuantityInStock + quantityRemoved;
        product.setQuantityInStock(newQuantityInStock);
        snackClient.deleteSnackFromCart(productId, newQuantityInStock);
        cart.updateProductQuantity(product, 0);
        removeProductFromOrderSummary(product.getProductId());
        System.out.println("Product quantity in cart: " + product.getQuantity());
        System.out.println("Product quantity in stock: " + product.getQuantityInStock());
        for (Product p : cart.getCartItems()) {
            System.out.println("Product in cart: " + p.getName());
        }
    }

    /**
     * Adds quantity to a snack. If the product has stock, it removes stock from the product, updates the quantity in the cart,
     * and prints the quantity in stock of the product. If the product does not have stock, it throws a NoMoreStockException.
     *
     * @param product the product to add quantity to
     * @throws NoMoreStockException if the product does not have stock
     */
    @Override
    public void addSnackQuantity(Product product) throws NoMoreStockException {
        if (product.getQuantityInStock() > 0) {
            product.removeStock();
            int productId = product.getProductId();
            int clientStock = product.getQuantityInStock();
            snackClient.addSnackQuantity(productId, clientStock);
            cart.updateProductQuantity(product, product.getQuantity() + 1);
            System.out.println("Product quantity in stock: " + product.getQuantityInStock());
        } else {
            throw new NoMoreStockException();
        }
    }

    /**
     * Removes quantity from a snack. If the new quantity is greater than 0, it adds stock to the product, updates the quantity in the cart,
     * and prints the quantity in stock of the product. If the new quantity is not greater than 0, it deletes the snack.
     *
     * @param product the product to remove quantity from
     */
    @Override
    public void removeSnackQuantity(Product product) {
        int productCurrentQuantity = product.getQuantity();
        int productNewQuantity = productCurrentQuantity - 1;
        if(productNewQuantity > 0) {
            product.addStock();
            int productId = product.getProductId();
            int clientStock = product.getQuantityInStock();
            snackClient.removeSnackQuantity(productId, clientStock);
            cart.updateProductQuantity(product, productNewQuantity);
            System.out.println("Product quantity in stock: " + product.getQuantityInStock());
        } else {
            deleteSnack(product);
        }
    }

    /**
     * Confirms the order. Generates the order summary and notifies the snack view controller that the order summary was created.
     */
    @Override
    public void confirmOrder() {
        cart.generateOrderSummary();
        snackViewController.orderSummaryCreated();
    }

    /**
     * Removes a product from the order summary.
     *
     * @param productId the id of the product to remove
     */
    private void removeProductFromOrderSummary(int productId) {
        snackViewController.removeProductFromOrderSummary(productId);
    }

    /**
     * Updates the total price of the cart in the snack view controller when the cart is updated.
     */
    @Override
    public void cartUpdated() {
        snackViewController.updateCartTotal(cart.getTotalPrice());
    }

}