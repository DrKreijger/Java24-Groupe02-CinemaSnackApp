package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderSummary {
    @SerializedName("products")
    private List<OrderSummaryProduct> products;

    @SerializedName("totalPrice")
    private double totalPrice;

    public OrderSummary(List<OrderSummaryProduct> products, double totalPrice) {
        this.products = products;
        this.totalPrice = totalPrice;
    }
}
