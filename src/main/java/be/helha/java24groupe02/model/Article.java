package be.helha.java24groupe02.model;

public class Article {
    private final String name;
    private final double price;

    public Article(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}
