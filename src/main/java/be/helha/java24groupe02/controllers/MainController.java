package be.helha.java24groupe02.controllers;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.CartObserver;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.models.exceptions.NoMoreStockException;
import be.helha.java24groupe02.views.SnackViewController;
import be.helha.java24groupe02.views.TemplateViewSnack.QuantityChangeListener;
import be.helha.java24groupe02.views.SnackViewController.CartListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class MainController extends Application implements CartListener, QuantityChangeListener, CartObserver {

    Cart cart;
    ProductDB productDB;
    private SnackViewController snackViewController;

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    @Override
    public void start(Stage stage) throws IOException {
        productDB = new ProductDB();
        List<Product> products = productDB.getAllProductsFromDatabase();
        cart = new Cart();
        cart.addObserver(this);

        FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
        Parent root = fxmlLoader.load();
        SnackViewController controller = fxmlLoader.getController();
        controller.initData(productDB, products, cart);
        controller.setCartListener(this);
        controller.setQuantityChangeListener(this);
        setSnackViewController(controller);

        Scene scene = new Scene(root);
        stage.setTitle("Snacks App");
        stage.setScene(scene);
        stage.show();
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


    @Override
    public void cartUpdated() {
        snackViewController.updateCartTotal(cart.getTotalPrice());
    }

}