package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur de vue pour la gestion des snacks.
 */
public class SnackViewController {


    TemplateViewButtonSnack templateViewButtonSnack = new TemplateViewButtonSnack();

    TemplateViewSnack templateViewSnack = new TemplateViewSnack();

    @FXML
    private FlowPane viewSnacksFlowPane;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private VBox viewOrderVBox;

    ProductDB productDB = new ProductDB();
    private List<Product> products = productDB.getAllProductsFromDatabase();
    private Product selectedProduct;
    private List<Product> cartItems = new ArrayList<>();


    /**
     * Initialise le contrôleur de vue. Charge les produits depuis la base de données et les ajoute à l'interface.
     */
    @FXML
    public void initialize() {
        for (Product product : this.products) {
            addSnackToInterface(product);
        }
        addSnackToOrderButton.setOnAction(event -> addProductToCart());
    }

    private void loadTemplateView(String snackName, String snackFlavor, String snackSize, double snackPrice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TemplateViewSnack.fxml"));
            Parent root = loader.load();
            TemplateViewSnack templateView = loader.getController();
            templateViewSnack.addSnackQuantityButton.setOnAction(event -> templateViewSnack.addQuantitySnackCart());

            // Vérifier que les labels sont correctement initialisés avant de les utiliser
            if(templateView.getNameSnackCart() != null) {
                templateView.getNameSnackCart().setText(snackName);
            }
            if(templateView.getFlavorSnackCart() != null) {
                templateView.getFlavorSnackCart().setText(snackFlavor);
            }
            if(templateView.getSizeSnackCart() != null) {
                templateView.getSizeSnackCart().setText(String.valueOf(snackSize)); // Convertir la taille en chaîne de caractères
            }
            if(templateView.getPriceSnackCart() != null) {
                templateView.getPriceSnackCart().setText(snackPrice + "€");
            }
            if (templateView.getImageSnackCart() != null) {
                templateView.getImageSnackCart().setImage(new Image("file:java.png"));
            }
            if (templateView.getQuantitySnackCart() != null) {
                templateView.getQuantitySnackCart().setText("1");
            }

            viewOrderVBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            updateCartTotal();
            loadTemplateView(selectedProduct.getName(), selectedProduct.getFlavor(), selectedProduct.getSize(), selectedProduct.getPrice());
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
}