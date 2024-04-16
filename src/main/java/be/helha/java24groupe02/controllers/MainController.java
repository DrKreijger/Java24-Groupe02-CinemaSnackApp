package be.helha.java24groupe02.controllers;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.ProductDB;
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

public class MainController extends Application {

    private Stage currentStage;
    ProductDB productDB = new ProductDB();
    private List<Product> products = productDB.getAllProductsFromDatabase();
    private Product selectedProduct;
    private Cart cart = new Cart();

    @Override
    public void start(Stage primaryStage) throws IOException {
        currentStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(SnackViewController.class.getResource("SnacksView.fxml"));
            Parent root = loader.load();
            SnackViewController controller = loader.getController();
            primaryStage.setTitle("Snacks App");
            primaryStage.setScene(new Scene(root));
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
}