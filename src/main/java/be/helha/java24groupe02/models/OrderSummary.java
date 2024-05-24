package be.helha.java24groupe02.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class represents an order summary in an e-commerce application.
 * It contains a list of products in the order and the total price of the order.
 */
public class OrderSummary {
    // List of products in the order
    @SerializedName("Products")
    private List<OrderSummaryProduct> products;

    // Total price of the order
    @SerializedName("Total order price")
    private double totalOrderPrice;

    /**
     * Constructs a new OrderSummary with the specified list of products and total order price.
     *
     * @param products the list of products in the order
     * @param totalOrderPrice the total price of the order
     */
    public OrderSummary(List<OrderSummaryProduct> products, double totalOrderPrice) {
        this.products = products;
        this.totalOrderPrice = totalOrderPrice;
    }
}
