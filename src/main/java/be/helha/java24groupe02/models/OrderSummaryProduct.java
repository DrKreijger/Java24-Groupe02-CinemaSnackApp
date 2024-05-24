package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a product in an order summary in an e-commerce application.
 * It contains the name, flavor, size, price per item, quantity, and total price of the product.
 */
public class OrderSummaryProduct {
    // Name of the product
    @SerializedName("Name")
    private String name;

    // Flavor of the product
    @SerializedName("Flavor")
    private String flavor;

    // Size of the product
    @SerializedName("Size")
    private String size;

    // Price per item of the product
    @SerializedName("Price per item")
    private double pricePerItem;

    // Quantity of the product in the order
    @SerializedName("Quantity")
    private int quantity;

    // Total price of the product in the order
    @SerializedName("Total price per item")
    private double totalPrice;

    /**
     * Constructs a new OrderSummaryProduct with the specified name, flavor, size, price per item, quantity, and total price.
     *
     * @param name the name of the product
     * @param flavor the flavor of the product
     * @param size the size of the product
     * @param pricePerItem the price per item of the product
     * @param quantity the quantity of the product in the order
     * @param totalPrice the total price of the product in the order
     */
    public OrderSummaryProduct(String name, String flavor, String size, double pricePerItem, int quantity, double totalPrice) {
        this.name = name;
        this.flavor = flavor;
        this.size = size;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
    }
}
