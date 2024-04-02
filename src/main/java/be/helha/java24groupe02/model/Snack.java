package be.helha.java24groupe02.model;

/**
 * Représente un snack avec un nom et un prix.
 */
public class Snack {
    private String name;
    private double price;

    /**
     * Constructeur pour initialiser un snack avec un nom et un prix.
     *
     * @param name  le nom du snack
     * @param price le prix du snack
     */
    public Snack(String name, double price) {
        this.name = name;
        this.price = price;
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
}
