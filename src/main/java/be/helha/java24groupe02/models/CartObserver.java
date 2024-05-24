package be.helha.java24groupe02.models;

/**
 * This interface defines a type of observer in the Observer Design Pattern.
 * It is used to observe changes in a Cart object in an e-commerce application.
 */
public interface CartObserver {
    /**
     * This method is called when the Cart object that this observer is observing is updated.
     * The observer should implement this method to handle the update.
     */
    void cartUpdated();
}
