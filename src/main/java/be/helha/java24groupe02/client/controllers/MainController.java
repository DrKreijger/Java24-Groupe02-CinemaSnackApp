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
    // The list of products
    private List<Product> products;

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
        this.products = updatedProducts;
        Platform.runLater(() -> {
            if (snackViewController == null) {
                try {
                    initializeView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    snackViewController.updateProducts(this.products);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initializes the view. Loads the FXML file, initializes the controller, and shows the stage.
     *
     * @throws IOException if an I/O error occurs
     */
    private void initializeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
        Parent root = fxmlLoader.load();
        initializeController(fxmlLoader);
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
     * @param fxmlLoader the FXMLLoader to get the controller from
     */
    private void initializeController(FXMLLoader fxmlLoader) {
        SnackViewController controller = fxmlLoader.getController();
        controller.initData(this.products, cart);
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

    @Override
public void onProductAddedToCart(Product product) {
    // Find the product in the global list
    Product globalProduct = products.stream()
        .filter(p -> p.getProductId() == product.getProductId())
        .findFirst()
        .orElse(null);

    if (globalProduct != null) {
        globalProduct.removeStock();
        int productId = globalProduct.getProductId();
        int clientStock = globalProduct.getQuantityInStock();
        snackClient.addToCart(productId, clientStock);
        cart.addProductToCart(globalProduct);
        System.out.println("Product quantity in stock: " + globalProduct.getQuantityInStock());
    }
}

@Override
public void deleteSnack(Product product) {
    // Find the product in the global list
    Product globalProduct = products.stream()
        .filter(p -> p.getProductId() == product.getProductId())
        .findFirst()
        .orElse(null);

    if (globalProduct != null) {
        int productId = product.getProductId();
        int currentQuantityInStock = globalProduct.getQuantityInStock();
        int quantityRemoved = product.getQuantity();
        int newQuantityInStock = currentQuantityInStock + quantityRemoved;
        product.setQuantityInStock(newQuantityInStock);
        System.out.println("Product removed from cart:" + product.getProductId());
        snackClient.deleteSnackFromCart(productId, newQuantityInStock);
        cart.updateProductQuantity(product, 0);
        removeProductFromOrderSummary(productId);
        System.out.println("Product quantity in cart: " + globalProduct.getQuantity());
        System.out.println("Product quantity in stock: " + globalProduct.getQuantityInStock());
        for (Product p : cart.getCartItems()) {
            System.out.println("Product in cart: " + p.getName());
        }
    }
}

@Override
public void addSnackQuantity(Product product) throws NoMoreStockException {
    // Find the product in the global list
    Product globalProduct = products.stream()
        .filter(p -> p.getProductId() == product.getProductId())
        .findFirst()
        .orElse(null);

    if (globalProduct != null) {
        if (globalProduct.getQuantityInStock() > 0) {
            globalProduct.removeStock();
            int productId = globalProduct.getProductId();
            int clientStock = globalProduct.getQuantityInStock();
            snackClient.addSnackQuantity(productId, clientStock);
            cart.updateProductQuantity(globalProduct, globalProduct.getQuantity() + 1);
            cart.updateProductQuantity(product, product.getQuantity() + 1);
            System.out.println("Product quantity in stock: " + globalProduct.getQuantityInStock());
        } else {
            throw new NoMoreStockException();
        }
    }
}

@Override
public void removeSnackQuantity(Product product) {
    // Find the product in the global list
    Product globalProduct = products.stream()
        .filter(p -> p.getProductId() == product.getProductId())
        .findFirst()
        .orElse(null);

    if (globalProduct != null) {
        int productNewQuantity = product.getQuantity() - 1;
        if(productNewQuantity > 0) {
            globalProduct.addStock();
            int productId = globalProduct.getProductId();
            int clientStock = globalProduct.getQuantityInStock();
            snackClient.removeSnackQuantity(productId, clientStock);
            cart.updateProductQuantity(globalProduct, globalProduct.getQuantity() - 1);
            cart.updateProductQuantity(product, product.getQuantity() - 1);
            System.out.println("Product quantity in stock: " + globalProduct.getQuantityInStock());
        } else {
            deleteSnack(product);
        }
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