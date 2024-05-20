package be.helha.java24groupe02.models.exceptions;

public class NoMoreStockException extends ExceptionsWithAlertTemplate {
    public NoMoreStockException() {
        super("Erreur de stock insuffisant", "Le stock actuel est insuffisant", "Veuillez réduire la quantité de snacks commandés.");
    }
}
