package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

public class OrderSummaryProduct {
    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("flavor")
    private String flavor;

    @SerializedName("size")
    private String size;

    @SerializedName("quantity")
    private int quantity;

    public OrderSummaryProduct(String name, String flavor, String size, double price, int quantity) {
        this.name = name;
        this.flavor = flavor;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters if necessary
}
