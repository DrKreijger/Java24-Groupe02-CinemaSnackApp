package be.helha.java24groupe02.server.models.exceptions;

import javafx.scene.control.Alert;

public class NoMoreStockException extends Throwable {
    public void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de stock insuffisant");
        alert.setHeaderText("Le stock actuel est insuffisant.");
        alert.setContentText("Veuillez réduire la quantité de snacks commandée.");
        alert.showAndWait();
    }
}
