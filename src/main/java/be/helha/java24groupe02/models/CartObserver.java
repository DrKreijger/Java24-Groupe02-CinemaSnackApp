package be.helha.java24groupe02.models;

import java.util.List;

public interface CartObserver {
    void update(List<Product> cartItems);
    void onTotalPriceUpdated(double totalPrice);
    void onProductAdded(Product product);
    void onProductRemoved(Product product);
}