package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Snacks;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class SnackViewController extends AnchorPane {

    @FXML
    private AnchorPane anchorPaneViewSnacks;

    @FXML
    private Button addSnackToOrderButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private AnchorPane anchorPaneViewOrder;

    private double currentXPosition = 10.0;
    private double totalPrice = 0.0;
    private List<Snacks> snacks = new ArrayList<>();
    private Snacks selectedSnacks;
    private List<Snacks> cartItems = new ArrayList<>();

    @FXML
    public void initialize() {
        Snacks popcorn = new Snacks("Popcorn", 2.50, "file:C:\\Users\\natha\\IdeaProjects\\Java24-Groupe02\\src\\main\\java\\be\\helha\\java24groupe02\\pictures\\Popcorn.png");
        Snacks cola = new Snacks("Coca-Cola", 1.50, "file:C:\\Users\\natha\\IdeaProjects\\Java24-Groupe02\\src\\main\\java\\be\\helha\\java24groupe02\\pictures\\CocaCola.png");
        Snacks chips = new Snacks("Chips", 3.00, "file:C:\\Users\\natha\\IdeaProjects\\Java24-Groupe02\\src\\main\\java\\be\\helha\\java24groupe02\\pictures\\Chips.png");

        snacks.add(popcorn);
        snacks.add(cola);
        snacks.add(chips);

        for (Snacks snack : snacks) {
            addSnackToInterface(snack);
        }
        addSnackToOrderButton.setOnAction(event -> addSnackToCart());
    }

    private void addSnackToInterface(Snacks snack) {
        VBox vbox = new VBox(5);

        Image image = new Image(snack.getImageUrl());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(75);
        imageView.setFitHeight(75);

        Text nameText = new Text(snack.getName());
        nameText.setStyle("-fx-font-weight: bold;");

        Text priceText = new Text("Prix: " + snack.getPrice() + "€");

        vbox.getChildren().addAll(imageView, nameText, priceText);

        Button snackButton = new Button();
        snackButton.setPrefWidth(75);
        snackButton.setPrefHeight(75);
        snackButton.setGraphic(vbox);

        snackButton.setOnAction(event -> handleSnackButtonClick(snack));

        snackButton.setId(snack.getName() + "Button");

        AnchorPane.setTopAnchor(snackButton, 10.0);
        AnchorPane.setLeftAnchor(snackButton, currentXPosition);

        anchorPaneViewSnacks.getChildren().add(snackButton);

        currentXPosition += 100.0 + 10.0;
    }

    private void handleSnackButtonClick(Snacks snack) {
        selectedSnacks = snack;
        updateSnackButtonsAppearance();
    }

    private void updateSnackButtonsAppearance() {
        for (Node node : anchorPaneViewSnacks.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Snacks snack = getSnackIdFromButton(button);

                if (selectedSnacks == snack) {
                    button.setStyle("-fx-background-color: lightblue;");
                } else {
                    button.setStyle("");
                }
            }
        }
    }

    private Snacks getSnackIdFromButton(Button button) {
        String buttonId = button.getId();
        for (Snacks snack : snacks) {
            if ((snack.getName() + "Button").equals(buttonId)) {
                return snack;
            }
        }
        return null;
    }

    private void addSnackToCart() {
        if (selectedSnacks != null) {
            cartItems.add(selectedSnacks);
            addSnackToOrderSummary();
            updateCartTotal();
        }
    }

    private void updateCartTotal() {
        double totalPrice = 0.0;
        for (Snacks snack : cartItems) {
            totalPrice += snack.getPrice();
        }
        totalPriceLabel.setText(totalPrice + "€");
    }

    private void addSnackToOrderSummary() {
        Image snackImage = new Image(selectedSnacks.getImageUrl());
        ImageView imageView = new ImageView(snackImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Text snackText = new Text(selectedSnacks.getName());
        Text priceText = new Text("Prix: " + selectedSnacks.getPrice() + "€");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(imageView, snackText, priceText);

        anchorPaneViewOrder.getChildren().add(vbox);
    }
}