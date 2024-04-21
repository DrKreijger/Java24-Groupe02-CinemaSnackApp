
package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartObserver> observers;
    private List<Product> cartItems = new ArrayList<>();
    private double totalPrice;

    public Cart() {
        this.observers = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addCartObserver(CartObserver observer) {
        observers.add(observer);
    }

    public void addProduct(Product product) {
        cartItems.add(product);
        totalPrice += product.getPrice();
        notifyObserversProductAdded(product);
        notifyObserversTotalPriceUpdated();
    }

    public void removeProduct(Product product) {
        cartItems.remove(product);
        totalPrice -= product.getPrice();
        notifyObserversProductRemoved(product);
        notifyObserversTotalPriceUpdated();
    }

    private void notifyObserversProductAdded(Product product) {
        for (CartObserver observer : observers) {
            observer.onProductAdded(product);
        }
    }

    private void notifyObserversProductRemoved(Product product) {
        for (CartObserver observer : observers) {
            observer.onProductRemoved(product);
        }
    }

    private void notifyObserversTotalPriceUpdated() {
        for (CartObserver observer : observers) {
            observer.onTotalPriceUpdated(totalPrice);
        }
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void addProductToCart(Product product) {
        cartItems.add(product);
        updateCartPrice();
    }

    public void updateProductQuantity(Product product, int quantity) {
        if (cartItems.contains(product)) {
            int index = cartItems.indexOf(product);
            Product productToUpdate = cartItems.get(index);
            if(quantity != 0) {
                productToUpdate.setQuantity(quantity);
            } else {
                cartItems.remove(product);
            }
            updateCartPrice();
        }
    }

    private void updateCartPrice() {
        this.totalPrice = 0;
        for (Product product : cartItems) {
            this.totalPrice += product.getPrice() * product.getQuantity();
        }
    }
}
