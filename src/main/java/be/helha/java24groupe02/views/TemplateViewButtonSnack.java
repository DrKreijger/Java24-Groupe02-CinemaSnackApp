package be.helha.java24groupe02.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TemplateViewButtonSnack {
    @FXML
    private Button ButtonSnackOrder;

    @FXML
    private ImageView ImageSnackOrder;

    @FXML
    private Label FlavorSnackOrder;

    @FXML
    private Label NameSnackOrder;

    @FXML
    private Label PriceSnackOrder;

    @FXML
    private Label SizeSnackOrder;

    public void initialize() {
    }

    public Label getFlavorSnackOrder() {
        return FlavorSnackOrder;
    }

    public Label getNameSnackOrder() {
        return NameSnackOrder;
    }

    public Label getPriceSnackOrder() {
        return PriceSnackOrder;
    }

    public Label getSizeSnackOrder() {
        return SizeSnackOrder;
    }

    public ImageView getImageSnackOrder() {
        return ImageSnackOrder;
    }

    public void setProductData(Product products) {
        // Charger l'image du snack
        Image productImage = new Image("file:java.png");
        ImageSnackOrder.setImage(productImage);

        // Configurer les autres données du produit
        NameSnackOrder.setText(products.getName());
        PriceSnackOrder.setText(products.getPrice() + "€");
        if (products.getFlavor() != null && !products.getFlavor().isEmpty()) {
            FlavorSnackOrder.setText(products.getFlavor());
        }
        SizeSnackOrder.setText(products.getSize());
    }
}
