package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contrôleur de vue pour la gestion des snacks.
 */
public class SnackViewController {

    @FXML
    private AnchorPane viewOrderAnchorPane;

    @FXML
    private FlowPane viewSnacksFlowPane;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private VBox viewOrderVBox;


    // Position horizontale initiale
    private double currentXPosition = 10.0;
    private List<Product> products = new ArrayList<>();
    private Product selectedProduct;
    private List<Product> cartItems = new ArrayList<>();

    /**
     * Initialise le contrôleur de vue.
     */
    @FXML
    public void initialize() {
        loadProductsFromDatabase();
        for (Product products : this.products) {
            addSnackToInterface(products);
        }
        addSnackToOrderButton.setOnAction(event -> addProductToCart());
    }

    /**
     * Ajoute un snack à l'interface utilisateur.
     *
     * @param products le snack à ajouter
     */
    private void addSnackToInterface(Product products) {
        // Créer une VBox pour organiser les éléments verticalement
        VBox vbox = new VBox(5); // espacement vertical entre les éléments

        // Charger l'image du snack
        Image productImage = new Image("file:java.png");

        // Créer l'imageView pour le snack
        ImageView imageView = new ImageView(productImage);
        imageView.setFitWidth(75); // largeur de l'image
        imageView.setFitHeight(75); // hauteur de l'image

        // Créer le texte pour le nom du snack
        Text productNameText = new Text(products.getName());
        productNameText.setStyle("-fx-font-weight: bold;"); // style pour mettre en gras

        // Créer le texte pour le prix du snack
        Text productPriceText = new Text("Prix: " + products.getPrice() + "€");

        // Créer le texte pour la saveur du snack
        Text productFlavorText = null;
        if (products.getFlavor() != null && !products.getFlavor().isEmpty()) {
            productFlavorText = new Text("Saveur: " + products.getFlavor());
        }

        // Créer le texte pour la taille du snack
        Text productSizeText = new Text("Taille: " + products.getSize());

        // Ajouter les éléments à la VBox
        vbox.getChildren().addAll(imageView, productNameText, productPriceText, productSizeText);
        if (productFlavorText != null) {
            vbox.getChildren().add(productFlavorText);
        }

        // Créer le bouton pour le snack
        Button snackButton = new Button();
        snackButton.setPrefWidth(75); // largeur du bouton
        snackButton.setPrefHeight(75); // hauteur du bouton
        snackButton.setGraphic(vbox); // définir la VBox comme graphique du bouton

        // Définir l'action à effectuer lors du clic sur le bouton
        snackButton.setOnAction(event -> handleSnackButtonClick(products));

        // Attribuer l'ID au bouton
        snackButton.setId(String.valueOf(products.getId()));

        // Définir la position du bouton dans l'AnchorPane
        AnchorPane.setTopAnchor(snackButton, 10.0); // position verticale
        AnchorPane.setLeftAnchor(snackButton, currentXPosition); // position horizontale

        // Ajouter le bouton à l'AnchorPane
        viewSnacksFlowPane.getChildren().add(snackButton);

        // Mettre à jour la position horizontale pour le prochain bouton
        currentXPosition += 100.0 + 10.0; // largeur du bouton + espacement horizontal
    }

    /**
     * Gère le clic sur un bouton de snack.
     *
     * @param products le snack associé au bouton cliqué
     */
    private void handleSnackButtonClick(Product products) {
        selectedProduct = products;
        updateProductButtonAppearance();
    }

    /**
     * Met à jour l'apparence des boutons de snacks.
     */
    private void updateProductButtonAppearance() {
        // Parcourir tous les boutons de snacks
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Product products = getProductIdFromButton(button);

                // Vérifier si le snack est sélectionné
                if (selectedProduct == products) {
                    // Mettre à jour l'apparence pour le snack sélectionné
                    button.setStyle("-fx-background-color: lightblue;");
                    System.out.println("Snack sélectionné : " + products.getId());
                } else {
                    // Mettre à jour l'apparence pour le snack non sélectionné
                    button.setStyle(""); // Reset style to default
                }
            }
        }
    }

    /**
     * Récupère le snack associé à un bouton.
     *
     * @param button le bouton associé au snack
     * @return le snack correspondant ou null si aucun snack correspondant n'est trouvé
     */
    private Product getProductIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Product products : this.products) {
            if ((String.valueOf(products.getId()).equals(buttonId))) {
                return products;
            }
        }
        return null; // Si aucun snack correspondant n'est trouvé
    }

    /**
     * Ajoute le snack sélectionné au panier.
     */
    private void addProductToCart() {
        if (selectedProduct != null) {
            cartItems.add(selectedProduct);
            addProductToOrderSummary();
            updateCartTotal();
        }
    }

    /**
     * Met à jour le prix total du panier.
     */
    private void updateCartTotal() {
        // Calculer le prix total
        double totalPrice = 0.0;
        for (Product products : cartItems) {
            // Ajouter le prix du snack au prix total
            totalPrice += products.getPrice();
        }
        // Afficher le prix total dans le label
        totalPriceLabel.setText(totalPrice + "€");
    }

    /**
     * Ajoute le snack sélectionné au récapitulatif de la commande.
     */
    private void addProductToOrderSummary() {
        // Créer le texte pour afficher le nom du snack dans anchorPaneViewOrder
        Text snackText = new Text(selectedProduct.getName() + "\n");

        // Ajouter le texte à viewOrderPane
        viewOrderVBox.getChildren().add(snackText);
    }

    private void loadProductsFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\DrKreijger\\Documents\\Cours\\Bloc 2 Desktop\\Java24-Groupe02\\snacks_simple.db");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Products"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setImagePath(resultSet.getString("image_path"));
                product.setCategory(resultSet.getString("category"));
                product.setFlavor(resultSet.getString("flavor"));
                product.setSize(resultSet.getString("size"));
                product.setPrice(resultSet.getDouble("price"));
                products.add(product);
            }
            resultSet.close();
            statement.close();
            connection.close();

            // Afficher les produits dans la console
            System.out.println("Liste des produits chargés depuis la base de données :");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
