package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Snacks;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur de vue pour la gestion des snacks.
 */
public class SnackViewController extends AnchorPane {

    @FXML
    private AnchorPane anchorPaneViewSnacks;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private AnchorPane anchorPaneViewOrder;

    // Position horizontale initiale
    private double currentXPosition = 10.0;
    private double totalPrice = 0.0;
    private List<Snacks> snacks = new ArrayList<>();
    private Snacks selectedSnacks;
    private List<Snacks> cartItems = new ArrayList<>();

    /**
     * Initialise le contrôleur de vue.
     */
    @FXML
    public void initialize() {
        // Ajouter des snacks
        Snacks popcorn = new Snacks("Popcorn", 2.50);
        Snacks cola = new Snacks("Coca-Cola", 1.50);
        Snacks chips = new Snacks("Chips", 3.00);

        snacks.add(popcorn);
        snacks.add(cola);
        snacks.add(chips);

        for (Snacks snacks : this.snacks) {
            addSnackToInterface(snacks);
        }
        addSnackToOrderButton.setOnAction(event -> addSnackToCart());
    }

    /**
     * Ajoute un snack à l'interface utilisateur.
     *
     * @param snacks le snack à ajouter
     */
    private void addSnackToInterface(Snacks snacks) {
        // Créer une VBox pour organiser les éléments verticalement
        VBox vbox = new VBox(5); // espacement vertical entre les éléments

        // Charger l'image du snack
        Image image = new Image("file:java.png");

        // Créer l'imageView pour le snack
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(75); // largeur de l'image
        imageView.setFitHeight(75); // hauteur de l'image

        // Créer le texte pour le nom du snack
        Text nameText = new Text(snacks.getName());
        nameText.setStyle("-fx-font-weight: bold;"); // style pour mettre en gras

        // Créer le texte pour le prix du snack
        Text priceText = new Text("Prix: " + snacks.getPrice() + "€");

        // Ajouter les éléments à la VBox
        vbox.getChildren().addAll(imageView, nameText, priceText);

        // Créer le bouton pour le snack
        Button snackButton = new Button();
        snackButton.setPrefWidth(75); // largeur du bouton
        snackButton.setPrefHeight(75); // hauteur du bouton
        snackButton.setGraphic(vbox); // définir la VBox comme graphique du bouton

        // Définir l'action à effectuer lors du clic sur le bouton
        snackButton.setOnAction(event -> handleSnackButtonClick(snacks));

        // Attribuer l'ID au bouton
        snackButton.setId(snacks.getName() + "Button");

        // Définir la position du bouton dans l'AnchorPane
        AnchorPane.setTopAnchor(snackButton, 10.0); // position verticale
        AnchorPane.setLeftAnchor(snackButton, currentXPosition); // position horizontale

        // Ajouter le bouton à l'AnchorPane
        anchorPaneViewSnacks.getChildren().add(snackButton);

        // Mettre à jour la position horizontale pour le prochain bouton
        currentXPosition += 100.0 + 10.0; // largeur du bouton + espacement horizontal
    }

    /**
     * Gère le clic sur un bouton de snack.
     *
     * @param snacks le snack associé au bouton cliqué
     */
    private void handleSnackButtonClick(Snacks snacks) {
        selectedSnacks = snacks;
        updateSnackButtonsAppearance();
    }

    /**
     * Met à jour l'apparence des boutons de snacks.
     */
    private void updateSnackButtonsAppearance() {
        // Parcourir tous les boutons de snacks
        for (Node node : anchorPaneViewSnacks.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Snacks snacks = getSnackIdFromButton(button);

                // Vérifier si le snack est sélectionné
                if (selectedSnacks == snacks) {
                    // Mettre à jour l'apparence pour le snack sélectionné
                    button.setStyle("-fx-background-color: lightblue;");
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
    private Snacks getSnackIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Snacks snacks : this.snacks) {
            if ((snacks.getName() + "Button").equals(buttonId)) {
                return snacks;
            }
        }
        return null; // Si aucun snack correspondant n'est trouvé
    }

    /**
     * Ajoute le snack sélectionné au panier.
     */
    private void addSnackToCart() {
        if (selectedSnacks != null) {
            cartItems.add(selectedSnacks);
            addSnackToOrderSummary();
            updateCartTotal();
        }
    }

    /**
     * Met à jour le prix total du panier.
     */
    private void updateCartTotal() {
        // Calculer le prix total
        double totalPrice = 0.0;
        for (Snacks snacks : cartItems) {
            // Ajouter le prix du snack au prix total
            totalPrice += snacks.getPrice();
        }
        // Afficher le prix total dans le label
        totalPriceLabel.setText(totalPrice + "€");
    }

    /**
     * Ajoute le snack sélectionné au récapitulatif de la commande.
     */
    private void addSnackToOrderSummary() {
        // Créer le texte pour afficher le nom du snack dans anchorPaneViewOrder
        Text snackText = new Text(selectedSnacks.getName());

        // Positionner le texte verticalement
        snackText.setLayoutY(anchorPaneViewOrder.getChildren().size() * 20); // Espacement vertical entre les snacks dans anchorPaneViewOrder

        // Ajouter le texte à anchorPaneViewOrder
        anchorPaneViewOrder.getChildren().add(snackText);
    }
}
