package be.helha.java24groupe02.client.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class TemplateViewButtonSnack {
    @FXML
    private VBox SnackOrderVbox;

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
        Image productImage = new Image("file:" + products.getImagePath());
        ImageSnackOrder.setImage(productImage);

        // Configurer les autres données du produit
        NameSnackOrder.setText(products.getName());
        IsFlavorNull(products);
        SizeSnackOrder.setText("Taille: " + products.getSize());
        PriceSnackOrder.setText("Prix: " + products.getPrice() + "€");
        ButtonSnackOrder.setId(String.valueOf(products.getProductId()));
    }

    private void IsFlavorNull(Product products) {
        if (products.getFlavor() != null && !products.getFlavor().isEmpty()) {
            FlavorSnackOrder.setText("Goût: " + products.getFlavor());
        } else {
            // Si le label de la saveur est vide, définir le texte du label de prix comme étant le seul texte de la VBox
            SnackOrderVbox.getChildren().remove(FlavorSnackOrder);
        }
    }
}
