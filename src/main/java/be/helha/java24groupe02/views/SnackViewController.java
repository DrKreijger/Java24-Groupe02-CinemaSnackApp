package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
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
 * View controller for the snacks view.
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


    // Position horizontal initial
    private double currentXPosition = 10.0;
    private List<Product> products = new ArrayList<>();
    private Product selectedProduct;
    private List<Product> cartItems = new ArrayList<>();

    /**
     * Initialise view controller. Load products from database and display them in the view.
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
     * Add a snack for the view.
     *
     * @param products snack to add
     */
    private void addSnackToInterface(Product products) {
        // Create VBox to organise éléments vertical
        VBox vbox = new VBox(5); // spacing between éléments

        // Load product image
        Image productImage = new Image("file:java.png");

        // Create ImageView for the snack image
        ImageView imageView = new ImageView(productImage);
        imageView.setFitWidth(75); // width of the image
        imageView.setFitHeight(75); // height of the image

        // Create Text for the snack name
        Text productNameText = new Text(products.getName());
        productNameText.setStyle("-fx-font-weight: bold;"); // bold font

        // Create Text for the snack price
        Text productPriceText = new Text("Prix: " + products.getPrice() + "€");

        // Create Text for the snack flavor
        Text productFlavorText = null;
        if (products.getFlavor() != null && !products.getFlavor().isEmpty()) {
            productFlavorText = new Text("Saveur: " + products.getFlavor());
        }

        // Create Text for the snack size
        Text productSizeText = new Text("Taille: " + products.getSize());

        // Add éléments to the VBox
        vbox.getChildren().addAll(imageView, productNameText, productPriceText, productSizeText);
        if (productFlavorText != null) {
            vbox.getChildren().add(productFlavorText);
        }

        // Create Button for the snack
        Button snackButton = new Button();
        snackButton.setPrefWidth(75); // width of the button
        snackButton.setPrefHeight(75); // height of the button
        snackButton.setGraphic(vbox); // add VBox to the button

        // Click event handler for the button
        snackButton.setOnAction(event -> handleSnackButtonClick(products));

        // Set the snack ID as the button ID
        snackButton.setId(String.valueOf(products.getId()));

        // Position the button in the AnchorPane
        AnchorPane.setTopAnchor(snackButton, 10.0); // position vertical
        AnchorPane.setLeftAnchor(snackButton, currentXPosition); // position horizontal

        // Add the button to the AnchorPane
        viewSnacksFlowPane.getChildren().add(snackButton);

        // Update the horizontal position for the next button
        currentXPosition += 100.0 + 10.0; // width of the button + spacing
    }

    /**
     * Click event handler for the snack button.
     *
     * @param products the snack associated with the button
     */
    private void handleSnackButtonClick(Product products) {
        selectedProduct = products;
        updateProductButtonAppearance();
    }

    /**
     * Update the appearance of the product buttons.
     */
    private void updateProductButtonAppearance() {
        // browse all buttons in the FlowPane
        for (Node node : viewSnacksFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Product products = getProductIdFromButton(button);

                // Check if the button is associated with the selected snack
                if (selectedProduct == products) {
                    // Update the appearance for the selected snack
                    button.setStyle("-fx-background-color: lightblue;");
                    System.out.println("Snack sélectionné : " + products.getId());
                } else {
                    // Reset the style to default
                    button.setStyle(""); // Reset style to default
                }
            }
        }
    }

    /**
     * Get the snack associated with the button.
     *
     * @param button the button associated with the snack
     * @return the snack associated with the button
     */
    private Product getProductIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Product products : this.products) {
            if ((String.valueOf(products.getId()).equals(buttonId))) {
                return products;
            }
        }
        return null; // Return null if no snack is found
    }

    /**
     * Add the selected snack for the cart.
     */
    private void addProductToCart() {
        if (selectedProduct != null) {
            cartItems.add(selectedProduct);
            addProductToOrderSummary();
            updateCartTotal();
        }
    }

    /**
     * Update the total price of the cart.
     */
    private void updateCartTotal() {
        // Set the total price to 0
        double totalPrice = 0.0;
        for (Product products : cartItems) {
            // Add the price of each snack for the total price
            totalPrice += products.getPrice();
        }
        // Update the total price label
        totalPriceLabel.setText(totalPrice + "€");
    }

    /**
     * Add the selected snack for the order summary.
     */
    private void addProductToOrderSummary() {
        // Create a grid pane to display the product details
        GridPane productGrid = new GridPane();
        productGrid.setPadding(new Insets(5));
        productGrid.setHgap(10);
        productGrid.setVgap(5);

        // Create labels for the product details
        Label nameLabel = new Label("Nom: " + selectedProduct.getName());
        Label priceLabel = new Label("Prix: " + selectedProduct.getPrice() + "€");
        Label flavorLabel = new Label("Goût: " + selectedProduct.getFlavor());
        Label sizeLabel = new Label("Taille: " + selectedProduct.getSize());

        // Add the labels to the grid pane
        productGrid.addRow(0, nameLabel);
        productGrid.addRow(1, priceLabel);
        productGrid.addRow(2, flavorLabel);
        productGrid.addRow(3, sizeLabel);

        // Set the style for the grid pane
        productGrid.setStyle("-fx-background-color: #FFFFFF;");

        // Add the grid pane to the order summary
        viewOrderVBox.getChildren().add(productGrid);
    }

    /**
     * Load products from the database.
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

            // Show products in console
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