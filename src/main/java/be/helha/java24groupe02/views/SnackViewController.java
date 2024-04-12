package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

import java.io.IOException;
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

    TemplateViewButtonSnack templateViewButtonSnack = new TemplateViewButtonSnack();
    @FXML
    public Label totalPriceLabel1;

    @FXML
    private FlowPane viewSnacksFlowPane;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private VBox viewOrderVBox;

    private List<Product> products = new ArrayList<>();
    private Product selectedProduct;
    private List<Product> cartItems = new ArrayList<>();

    /**
     * Initialise le contrôleur de vue. Charge les produits depuis la base de données et les ajoute à l'interface.
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
       FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewButtonSnack.fxml"));
        try{
            Button snackButton = loader.load();
            TemplateViewButtonSnack controller = loader.getController();

            controller.setProductData(products);

            snackButton.setOnAction(event -> handleSnackButtonClick(products));

            viewSnacksFlowPane.getChildren().add(snackButton);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
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
        totalPriceLabel1.setText(totalPrice + "€");
    }

    /**
     * Ajoute le snack sélectionné au récapitulatif de la commande.
     */
    private void addProductToOrderSummary() {
        // Créer un GridPane pour organiser les informations du produit
        GridPane productGrid = new GridPane();
        productGrid.setPadding(new Insets(5));
        productGrid.setHgap(10);
        productGrid.setVgap(5);

        // Ajouter le nom, le prix, le goût et la taille du produit dans des labels
        Label nameLabel = new Label("Nom: " + selectedProduct.getName());
        Label priceLabel = new Label("Prix: " + selectedProduct.getPrice() + "€");
        Label flavorLabel = new Label("Goût: " + selectedProduct.getFlavor());
        Label sizeLabel = new Label("Taille: " + selectedProduct.getSize());

        // Ajouter les labels au GridPane
        productGrid.addRow(0, nameLabel);
        productGrid.addRow(2, flavorLabel);
        productGrid.addRow(3, sizeLabel);

        // Appliquer un arrière-plan blanc au GridPane
        productGrid.setStyle("-fx-background-color: #FFFFFF;");

        // Ajouter le GridPane à votre VBox
        viewOrderVBox.getChildren().add(productGrid);
        // Ajouter le label du prix en dehors du GridPane à votre VBox
        viewOrderVBox.getChildren().add(priceLabel);
    }

    /**
     * Charge les produits depuis la base de données.
     */
    private void loadProductsFromDatabase() {
        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:snacks_simple.db");
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