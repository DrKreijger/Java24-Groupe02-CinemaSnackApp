package be.helha.java24groupe02.models;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imagePath;
    private String flavor;
    private String size;
    private int quantity;

    public Product(int id, String name, double price, String imagePath, String flavor, String size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.flavor = flavor;
        this.size = size;
        this.quantity = 1;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
