package be.helha.java24groupe02.models.exceptions;

public class ProductLoadingException extends ExceptionsWithAlertTemplate {
    public ProductLoadingException() {
        super("Erreur de chargement des produits", "Erreur lors du chargement des produits", "Veuillez réessayer plus tard.");
    }
}
