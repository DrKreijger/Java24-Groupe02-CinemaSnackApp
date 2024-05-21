package be.helha.java24groupe02.server.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderSummary {
    @SerializedName("Products")
    private List<OrderSummaryProduct> products;

    @SerializedName("Total order price")
    private double totalOrderPrice;

    public OrderSummary(List<OrderSummaryProduct> products, double totalOrderPrice) {
        this.products = products;
        this.totalOrderPrice = totalOrderPrice;
    }
}
