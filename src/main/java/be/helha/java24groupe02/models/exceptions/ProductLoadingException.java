package be.helha.java24groupe02.models.exceptions;

/**
 * This class extends the ExceptionsWithAlertTemplate class and is used to create a custom exception for when there is an error loading products.
 * It contains a constructor that sets the title, header text, and content text of the alert dialog to be shown when the exception is thrown.
 */
public class ProductLoadingException extends ExceptionsWithAlertTemplate {
    /**
     * Constructs a new ProductLoadingException with a predefined alert title, header text, and content text.
     * The alert title is set to "Erreur de chargement des produits", the header text is set to "Erreur lors du chargement des produits",
     * and the content text is set to "Veuillez réessayer plus tard.".
     */
    public ProductLoadingException() {
        super("Erreur de chargement des produits", "Erreur lors du chargement des produits", "Veuillez réessayer plus tard.");
    }
}
