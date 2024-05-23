package be.helha.java24groupe02.client.controllers;

import be.helha.java24groupe02.client.network.SnackClient;
import be.helha.java24groupe02.client.views.SnackViewController;
import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.CartObserver;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import be.helha.java24groupe02.client.views.TemplateViewSnack.QuantityChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class MainController extends Application implements SnackViewController.CartListener, QuantityChangeListener, CartObserver {

    private Cart cart;
    private SnackViewController snackViewController;
    private SnackClient snackClient;

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    @Override
    public void start(Stage stage) {
            try {
                initializeCart();
                initializeNetworkClient();
            } catch (Exception e) {
                Platform.exit();
            }
    }

    public static void main(String[] args) {
        launch();
    }

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

    private void initializeView(List<Product> products) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
        Parent root = fxmlLoader.load();
        initializeController(products, fxmlLoader);
        initializeStage(root);
    }

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

    private void initializeController(List<Product> products, FXMLLoader fxmlLoader) {
        SnackViewController controller = fxmlLoader.getController();
        controller.initData(products, cart);
        controller.setCartListener(this);
        controller.setQuantityChangeListener(this);
        setSnackViewController(controller);
    }

    private void initializeNetworkClient() {
        snackClient = new SnackClient();
        new Thread(() -> {
            snackClient.start();
            snackClient.setProductsUpdateListener(this::updateProductsFromServer);
        }).start();
    }

    private void initializeCart() {
        cart = new Cart();
        cart.addObserver(this);
    }

    @Override
    public void onProductAddedToCart(Product product) {
            product.removeStock();
            int productId = product.getProductId();
            int clientStock = product.getQuantityInStock();
            snackClient.addToCart(productId, clientStock);
            cart.addProductToCart(product);
            System.out.println("Product quantity in stock: " + product.getQuantityInStock());
    }

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
        System.out.println("Product quantity in stock: " + product.getQuantityInStock());
    }

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

    @Override
    public void removeSnackQuantity(Product product) {
        int productCurrentQuantity = product.getQuantity();
        int productNewQuantity = productCurrentQuantity - 1;
        if(productNewQuantity > 0) {
            product.addStock();
            int productId = product.getProductId();
            int clientStock = product.getQuantityInStock();
            snackClient.removeSnackQuantity(productId, clientStock);
            cart.updateProductQuantity(product, 0);
        } else {
            deleteSnack(product);
        }
    }

    private void removeProductFromOrderSummary(int productId) {
        snackViewController.removeProductFromOrderSummary(productId);
    }

    @Override
    public void cartUpdated() {
        snackViewController.updateCartTotal(cart.getTotalPrice());
    }

}