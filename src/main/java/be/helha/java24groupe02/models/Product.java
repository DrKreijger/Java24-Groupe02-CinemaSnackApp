package be.helha.java24groupe02.models;

import java.io.Serializable;
import java.net.URL;

/**
 * This class represents a product in an e-commerce application.
 * It contains the product ID, name, image path, flavor, size, price, quantity, and quantity in stock.
 * It implements the Serializable interface, which means it can be converted to a byte stream and restored later.
 */
public class Product implements Serializable {

    // The product ID
    private final int productId;
    // The name of the product
    private final String name;
    // The image path of the product
    private final URL imagePath;
    // The flavor of the product
    private final String flavor;
    // The size of the product
    private final String size;
    // The price of the product
    private final double price;
    // The quantity of the product
    private int quantity;
    // The quantity of the product in stock
    private int quantityInStock;

    /**
     * Constructs a new Product with the specified product ID, name, image path, flavor, size, price, and quantity in stock.
     * The quantity is initially set to 1.
     *
     * @param productId the product ID
     * @param name the name of the product
     * @param imagePath the image path of the product
     * @param flavor the flavor of the product
     * @param size the size of the product
     * @param price the price of the product
     * @param quantityInStock the quantity of the product in stock
     */
    public Product(int productId, String name, URL imagePath, String flavor, String size, double price, int quantityInStock) {
        this.productId = productId;
        this.name = name;
        this.imagePath = imagePath;
        this.flavor = flavor;
        this.size = size;
        this.price = price;
        this.quantity = 1;
        this.quantityInStock = quantityInStock;
    }

    // Getter and setter methods for the product properties

    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the image path of the product.
     *
     * @return the image path of the product
     */
    public URL getImagePath() {
        return imagePath;
    }

    /**
     * Returns the flavor of the product.
     *
     * @return the flavor of the product
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * Returns the size of the product.
     *
     * @return the size of the product
     */
    public String getSize() {
        return size;
    }

    /**
     * Returns the quantity of the product.
     *
     * @return the quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product.
     *
     * @param quantity the new quantity of the product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the quantity of the product in stock.
     *
     * @return the quantity of the product in stock
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity of the product in stock.
     *
     * @param quantityInStock the new quantity of the product in stock
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Increases the quantity of the product in stock by 1.
     */
    public void addStock(){
        this.quantityInStock++;
    }

    /**
     * Decreases the quantity of the product in stock by 1 if the quantity in stock is greater than 0.
     */
    public void removeStock(){
        if (this.quantityInStock > 0){
            this.quantityInStock--;
        }
    }
}
