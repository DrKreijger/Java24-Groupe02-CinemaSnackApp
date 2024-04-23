package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> cartItems;
    private List<CartObserver> observerList;
    private double totalPrice;

    public Cart() {
        this.observerList = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addObserver(CartObserver observer) {
        observerList.add(observer);
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Product> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addProductToCart(Product product) {
        cartItems.add(product);
        updateCartPrice();
    }

    public void removeProductFromCart(Product product) {
        cartItems.remove(product);
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
        notifyCartObservers();
    }

    private void notifyCartObservers() {
        for (CartObserver observer : observerList) {
            observer.cartUpdated();
        }
    }
}
