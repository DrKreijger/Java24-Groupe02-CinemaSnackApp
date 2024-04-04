package be.helha.java24groupe02.models;

/**
 * Représente un snack avec un nom, un prix et une URL d'image.
 */
public class Snacks {
    private String name;
    private double price;
    private String imageUrl; // URL de l'image du snack

    /**
     * Constructeur pour initialiser un snack avec un nom, un prix et une URL d'image.
     *
     * @param name     le nom du snack
     * @param price    le prix du snack
     * @param imageUrl l'URL de l'image du snack
     */
    public Snacks(String name, double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    /**
     * Obtient le nom du snack.
     *
     * @return le nom du snack
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom du snack.
     *
     * @param name le nouveau nom du snack
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtient le prix du snack.
     *
     * @return le prix du snack
     */
    public double getPrice() {
        return price;
    }

    /**
     * Définit le prix du snack.
     *
     * @param price le nouveau prix du snack
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtient l'URL de l'image du snack.
     *
     * @return l'URL de l'image du snack
     */
    public String getImageUrl() {
        return imageUrl;
    }
}