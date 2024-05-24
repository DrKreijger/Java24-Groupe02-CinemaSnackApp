package be.helha.java24groupe02.models.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class extends the Exception class and is used to create custom exceptions with alert dialogs in a JavaFX application.
 * It contains the title, header text, and content text of the alert dialog.
 */
public class ExceptionsWithAlertTemplate extends Exception {
    // The title of the alert dialog
    private final String alertTitle;
    // The header text of the alert dialog
    private final String alertHeaderText;
    // The content text of the alert dialog
    private final String alertContentText;

    /**
     * Constructs a new ExceptionsWithAlertTemplate with the specified alert title, header text, and content text.
     *
     * @param alertTitle the title of the alert dialog
     * @param alertHeaderText the header text of the alert dialog
     * @param alertContentText the content text of the alert dialog
     */
    public ExceptionsWithAlertTemplate(String alertTitle, String alertHeaderText, String alertContentText) {
        this.alertTitle = alertTitle;
        this.alertHeaderText = alertHeaderText;
        this.alertContentText = alertContentText;
    }

    /**
     * Shows an error alert dialog with the title, header text, and content text specified in the constructor.
     * The alert dialog is modal and blocks user input until it is dismissed.
     */
    public void showError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeaderText);
        alert.setContentText(alertContentText);
        alert.showAndWait();
    }
}
