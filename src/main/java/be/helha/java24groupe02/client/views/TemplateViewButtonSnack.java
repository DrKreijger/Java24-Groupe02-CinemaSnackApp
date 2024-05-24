package be.helha.java24groupe02.client.views;

import be.helha.java24groupe02.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;

/**
 * This class represents a template view for a snack button in a JavaFX application.
 * It manages the display of product data in the user interface.
 */
public class TemplateViewButtonSnack {

    // JavaFX UI components
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

    /**
     * Initializes the view. This method is called after all @FXML annotated members have been injected.
     */
    public void initialize() {
    }

    /**
     * Sets the product data to be displayed in the view.
     *
     * @param products the product whose data is to be displayed
     */
    public void setProductData(Product products) {
        // Get the product image URL and create an Image object
        URL productImageURL = (products.getImagePath());
        Image productImage = new Image(productImageURL.toExternalForm());

        // Set the product image, name, size, price, and id
        ImageSnackOrder.setImage(productImage);
        NameSnackOrder.setText(products.getName());
        IsFlavorNull(products);
        SizeSnackOrder.setText("Taille: " + products.getSize());
        PriceSnackOrder.setText("Prix: " + products.getPrice() + "€");
        ButtonSnackOrder.setId(String.valueOf(products.getProductId()));
    }

    /**
     * Checks if the product has a flavor. If it does, sets the flavor text.
     * If it does not, removes the flavor label from the view.
     *
     * @param products the product to check the flavor of
     */
    private void IsFlavorNull(Product products) {
        if (products.getFlavor() != null && !products.getFlavor().isEmpty()) {
            FlavorSnackOrder.setText("Goût: " + products.getFlavor());
        } else {
            SnackOrderVbox.getChildren().remove(FlavorSnackOrder);
        }
    }
}
