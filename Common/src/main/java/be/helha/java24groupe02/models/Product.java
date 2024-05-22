package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    private int productId;
    private String name;
    private String imagePath;
    private String flavor;
    private String size;
    private double price;
    private int quantity;
    private int quantityInStock;

    public Product(int productId, String name, String imagePath, String flavor, String size,double price, int quantityInStock) {
        this.productId = productId;
        this.name = name;
        this.imagePath = imagePath;
        this.flavor = flavor;
        this.size = size;
        this.price = price;
        this.quantity = 1;
        this.quantityInStock = quantityInStock;
    }


    public int getProductId() {
        return productId;
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

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void addStock(){
        this.quantityInStock++;
    }

    public void removeStock(){
        if (this.quantityInStock > 0){
            this.quantityInStock--;
        }
    }

    public String getSummary() {
        return "Nom: " + name + ", Taille: " + size + ", Goût: " + flavor + ", Quantité: " + quantity + ", Prix: " + price + "€";
    }

}