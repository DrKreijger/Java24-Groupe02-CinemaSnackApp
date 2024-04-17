package be.helha.java24groupe02.controllers;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.views.SnackViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MainController extends Application implements SnackViewController.CartListener {

    Cart cart;

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
}