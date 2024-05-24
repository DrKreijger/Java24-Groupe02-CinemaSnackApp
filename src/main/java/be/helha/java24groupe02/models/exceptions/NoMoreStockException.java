package be.helha.java24groupe02.models.exceptions;

/**
 * This class extends the ExceptionsWithAlertTemplate class and is used to create a custom exception for when there is no more stock of a product.
 * It contains a constructor that sets the title, header text, and content text of the alert dialog to be shown when the exception is thrown.
 */
public class NoMoreStockException extends ExceptionsWithAlertTemplate {
    /**
     * Constructs a new NoMoreStockException with a predefined alert title, header text, and content text.
     * The alert title is set to "Erreur de stock insuffisant", the header text is set to "Le stock actuel est insuffisant",
     * and the content text is set to "Veuillez réduire la quantité de snacks commandés.".
     */
    public NoMoreStockException() {
        super("Erreur de stock insuffisant", "Le stock actuel est insuffisant", "Veuillez réduire la quantité de snacks commandés.");
    }
}
