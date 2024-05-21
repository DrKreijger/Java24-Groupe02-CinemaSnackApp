package be.helha.java24groupe02.client.controllers;

import be.helha.java24groupe02.client.network.SnackClient;
import be.helha.java24groupe02.client.views.SnackViewController;
import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.CartObserver;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import be.helha.java24groupe02.models.exceptions.ProductLoadingException;
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
    private ProductDB productDB;
    private SnackViewController snackViewController;
    private SnackClient snackClient;

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            List<Product> products = getProductsFromDB();
            initializeCart();
            initializeView(stage, products);
            initializeNetworkClient();
        } catch (ProductLoadingException e) {
            e.showError();
        }
    }

    private void initializeNetworkClient() {
        snackClient = new SnackClient();
        new Thread(() -> {
            snackClient.start();
            snackClient.setProductsUpdateListener(this::updateProductsFromServer);
        }).start();
    }

    private void updateProductsFromServer(List<Product> updatedProducts) {
        Platform.runLater(() -> {
            snackViewController.updateProducts(updatedProducts);
        });
    }

    private void initializeView(Stage stage, List<Product> products) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
        Parent root = fxmlLoader.load();
        initializeController(products, fxmlLoader);
        initializeStage(stage, root);
    }

    private static void initializeStage(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setTitle("Snacks App");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeController(List<Product> products, FXMLLoader fxmlLoader) {
        SnackViewController controller = fxmlLoader.getController();
        controller.initData(productDB, products, cart);
        controller.setCartListener(this);
        controller.setQuantityChangeListener(this);
        setSnackViewController(controller);
    }

    private void initializeCart() {
        cart = new Cart();
        cart.addObserver(this);
    }

    private List<Product> getProductsFromDB() throws ProductLoadingException {
        try{
            productDB = new ProductDB();
            initializeStock();
            List<Product> products = productDB.getAllProductsFromDatabase();
            return products;
        } catch (ProductLoadingException e) {
            throw new ProductLoadingException();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void onProductAddedToCart(Product product) {
            removeStock(product);
            cart.addProductToCart(product);
    }

    @Override
    public void deleteSnack(Product product) {
       int productId = product.getProductId();
       int currentQuantityIncart = product.getQuantity();
       updateStock(product, currentQuantityIncart);
       removeProductFromCart(productId);
       cart.updateProductQuantity(product, 0);
    }

    @Override
    public void addSnackQuantity(Product product) throws NoMoreStockException {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity + 1;
        if (product.getQuantityInStock() > 0) {
            removeStock(product);
            cart.updateProductQuantity(product, newQuantity);
        } else {
            throw new NoMoreStockException();
        }
    }

    @Override
    public void removeSnackQuantity(Product product) {
        int currentQuantity = product.getQuantity();
        int newQuantity = currentQuantity - 1;
        if(newQuantity !=0) {
            addStock(product);
            cart.updateProductQuantity(product, newQuantity);
        } else {
            deleteSnack(product);
        }
    }

    private void  removeProductFromCart(int productId) {
        snackViewController.removeProductFromOrderSummary(productId);
    }

    private void addStock(Product product) {
        int productId = product.getProductId();
        product.addStock();
        productDB.updateProductQuantityInStock(productId, product.getQuantityInStock());
    }

    private void removeStock(Product product) {
        int productId = product.getProductId();
        int productQuantityInStock = product.getQuantityInStock();
        if(productQuantityInStock > 0) {
            product.removeStock();
            productDB.updateProductQuantityInStock(productId, product.getQuantityInStock());
        } else {
            System.err.println("Stock insuffisant");
        }
    }

    private void updateStock(Product product, int quantity) {
        int productId = product.getProductId();
        int currentQuantityInStock = product.getQuantityInStock();
        int newQuantityInStock = currentQuantityInStock + quantity;
        product.setQuantityInStock(newQuantityInStock);
        productDB.updateProductQuantityInStock(productId, newQuantityInStock);
    }

    private void initializeStock() {
        ProductDB productDB = new ProductDB();
        productDB.initializeStockToDefault();
    }

    @Override
    public void cartUpdated() {
        snackViewController.updateCartTotal(cart.getTotalPrice());
    }

}