package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

public class OrderSummaryProduct {
    @SerializedName("name")
    private String name;

    @SerializedName("flavor")
    private String flavor;

    @SerializedName("size")
    private String size;

    @SerializedName("price")
    private double pricePerItem;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("totalPrice")
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
