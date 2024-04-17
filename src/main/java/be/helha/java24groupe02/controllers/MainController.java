package be.helha.java24groupe02.controllers;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.views.SnackViewController.*;
import be.helha.java24groupe02.views.SnackViewController;
import be.helha.java24groupe02.models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MainController extends Application implements SnackViewListener {

    SnackViewController snackViewController;
    private Stage currentStage;
    ProductDB productDB;
    private List<Product> products;
    private Product selectedProduct;
    private Cart cart;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.currentStage = primaryStage;
        productDB = new ProductDB();
        products = productDB.getAllProductsFromDatabase();
        cart = new Cart();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            SnackViewController controller = fxmlLoader.getController();
            controller.setListener(this);
            controller.setProducts(products);

            primaryStage.setTitle("Snacks App");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            showErrorAlert();
        }
    }

    private static void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur lors du chargement de la fenêtre.");
        alert.setHeaderText("Une erreur est survenue lors du chargement de la fenêtre.");
        alert.setContentText("C'est un erreur interne. Veuillez contacter l'équipe des développeurs t'as peur.");
    }

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void handleSnackButtonClick(Product products) {
        selectedProduct = products;
    }

    @Override
    public void addProductToOrder(Product selectedProduct) {
        cart.addProductToCart(selectedProduct);
    }
}