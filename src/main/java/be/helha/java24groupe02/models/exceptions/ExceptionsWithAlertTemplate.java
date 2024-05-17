package be.helha.java24groupe02.models.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionsWithAlertTemplate extends Exception {
    private String alertTitle;
    private String alertHeaderText;
    private String alertContentText;

    public ExceptionsWithAlertTemplate(String alertTitle, String alertHeaderText, String alertContentText) {
        this.alertTitle = alertTitle;
        this.alertHeaderText = alertHeaderText;
        this.alertContentText = alertContentText;
    }

    public void showError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeaderText);
        alert.setContentText(alertContentText);
        alert.showAndWait();
    }
}
