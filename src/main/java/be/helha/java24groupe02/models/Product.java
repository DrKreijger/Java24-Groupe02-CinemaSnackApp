package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class Product {
    private List<CartObserver> observers = new ArrayList<>();
    private void notifyObservers(List<Product> cartItems) {
        for (CartObserver observer : observers) {
            observer.update(cartItems);
        }
    }

    private int id;
    private String name;
    private double price;
    private String category;
    private String imagePath;
    private String flavor;
    private String size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
