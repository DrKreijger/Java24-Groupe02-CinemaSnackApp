package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

public class OrderSummaryProduct {
    @SerializedName("Name")
    private String name;

    @SerializedName("Flavor")
    private String flavor;

    @SerializedName("Size")
    private String size;

    @SerializedName("Price per item")
    private double pricePerItem;

    @SerializedName("Quantity")
    private int quantity;

    @SerializedName("Total price per item")
    private double totalPrice;

    public OrderSummaryProduct(String name, String flavor, String size, double pricePerItem, int quantity, double totalPrice) {
        this.name = name;
        this.flavor = flavor;
        this.size = size;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
    }

    // Getters and setters if necessary
}
