package be.helha.java24groupe02.controllers;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.views.SnackViewController;
import be.helha.java24groupe02.views.TemplateViewSnack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class MainController extends Application implements SnackViewController.CartListener, TemplateViewSnack.QuantityChangeListener{

    Cart cart;
    private SnackViewController snackViewController;

    public void setSnackViewController(SnackViewController snackViewController) {
        this.snackViewController = snackViewController;
    }

    @Override
    public void start(Stage stage) throws IOException {
        ProductDB productDB = new ProductDB();
        List<Product> products = productDB.getAllProductsFromDatabase();
        cart = new Cart();

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
        cart.addProductToCart(product);
    }

    @Override
    public void onQuantityChanged(Product product, int quantity) {
       int productId = product.getId();
        if(quantity != 0) {
            cart.updateProductQuantity(product, quantity);
        } else {
            removeProductFromCart(productId);
            cart.updateProductQuantity(product, quantity);
        }
        updateCartTotal();
    }

    private void updateCartTotal() {
        snackViewController.updateCartTotal();
    }

    private void  removeProductFromCart(int productId) {
        snackViewController.removeProductFromOrderSummary(productId);
    }
}