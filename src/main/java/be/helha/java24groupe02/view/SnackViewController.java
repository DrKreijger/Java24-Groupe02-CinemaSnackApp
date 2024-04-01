package be.helha.java24groupe02.view;

import be.helha.java24groupe02.model.Snack;
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

public class SnackViewController extends AnchorPane {
    @FXML
    private AnchorPane anchorPaneViewSnacks;
    @FXML
    private Button addSnackToOrderButton;
    @FXML
    private Label totalPriceLabel;

    // Position horizontale initiale
    private double currentXPosition = 10.0;
    private double totalPrice = 0.0;
    private List<Snack> snacks = new ArrayList<>();
    private List<Snack> selectedSnacks = new ArrayList<>();

    @FXML
    public void initialize() {
        // Ajouter des snacks
        Snack popcorn = new Snack("Popcorn", 2.50);
        Snack cola = new Snack("Coca-Cola", 1.50);
        Snack chips = new Snack("Chips", 3.00);

        snacks.add(popcorn);
        snacks.add(cola);
        snacks.add(chips);

        for (Snack snack : snacks) {
            addSnackToInterface(snack);
        }
        addSnackToOrderButton.setOnAction(event -> addSnackToOrder());
    }

    private void addSnackToInterface(Snack snack) {
        // Créer une VBox pour organiser les éléments verticalement
        VBox vbox = new VBox(5); // espacement vertical entre les éléments

        // Charger l'image du snack
        Image image = new Image("file:java.png");

        // Créer l'imageView pour le snack
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(75); // largeur de l'image
        imageView.setFitHeight(75); // hauteur de l'image

        // Créer le texte pour le nom du snack
        Text nameText = new Text(snack.getName());
        nameText.setStyle("-fx-font-weight: bold;"); // style pour mettre en gras

        // Créer le texte pour le prix du snack
        Text priceText = new Text("Prix: " + snack.getPrice() + "€");

        // Ajouter les éléments à la VBox
        vbox.getChildren().addAll(imageView, nameText, priceText);

        // Créer le bouton pour le snack
        Button snackButton = new Button();
        snackButton.setPrefWidth(75); // largeur du bouton
        snackButton.setPrefHeight(75); // hauteur du bouton
        snackButton.setGraphic(vbox); // définir la VBox comme graphique du bouton

        // Définir l'action à effectuer lors du clic sur le bouton
        snackButton.setOnAction(event -> handleSnackButtonClick(snack));

        // Attribuer l'ID au bouton
        snackButton.setId(snack.getName() + "Button");

        // Définir la position du bouton dans l'AnchorPane
        AnchorPane.setTopAnchor(snackButton, 10.0); // position verticale
        AnchorPane.setLeftAnchor(snackButton, currentXPosition); // position horizontale

        // Ajouter le bouton à l'AnchorPane
        anchorPaneViewSnacks.getChildren().add(snackButton);

        // Mettre à jour la position horizontale pour le prochain bouton
        currentXPosition += 100.0 + 10.0; // largeur du bouton + espacement horizontal
    }

    private void handleSnackButtonClick(Snack snack) {
       if (selectedSnacks.contains(snack)) {
            selectedSnacks.remove(snack);
            System.out.println("Removed " + snack.getName());
        } else {
           selectedSnacks.clear();
           selectedSnacks.add(snack);
           System.out.println("Selected " + snack.getName());
       }
        updateSnackButtonsAppearance();
    }

    private void updateSnackButtonsAppearance() {
        // Parcourir tous les boutons de snacks
        for (Node node : anchorPaneViewSnacks.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Snack snack = getSnackIdFromButton(button);

                // Vérifier si le snack est sélectionné
                if (selectedSnacks.contains(snack)) {
                    // Mettre à jour l'apparence pour le snack sélectionné
                    button.setStyle("-fx-background-color: lightblue;");
                } else {
                    // Mettre à jour l'apparence pour le snack non sélectionné
                    button.setStyle(""); // Reset style to default
                }
            }
        }
    }

    private Snack getSnackIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Snack snack : snacks) {
            if ((snack.getName() + "Button").equals(buttonId)) {
                return snack;
            }
        }
        return null; // Si aucun snack correspondant n'est trouvé
    }


    private void addSnackToOrder(){
        // Ajouter le snack à la commande
        for (Snack snack : selectedSnacks) {
            totalPrice += snack.getPrice();
        }
        totalPriceLabel.setText(totalPrice + "€");
    }
}