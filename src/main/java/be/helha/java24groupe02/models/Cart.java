package be.helha.java24groupe02.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents a shopping cart in an e-commerce application.
 * It manages the products added to the cart, the total price of the cart, and notifies observers when the cart is updated.
 */
public class Cart {
    // List of products in the cart
    private final List<Product> cartItems;
    // List of observers to be notified when the cart is updated
    private final List<CartObserver> observerList;
    // Total price of the products in the cart
    private double totalPrice;

    /**
     * Constructs a new Cart with an empty list of products, an empty list of observers, and a total price of 0.
     */
    public Cart() {
        this.observerList = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer the observer to add
     */
    public void addObserver(CartObserver observer) {
        observerList.add(observer);
    }

    /**
     * Returns the list of products in the cart.
     *
     * @return the list of products in the cart
     */
    public List<Product> getCartItems() {
        return cartItems;
    }

    /**
     * Returns the total price of the products in the cart.
     *
     * @return the total price of the products in the cart
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Adds a product to the cart and updates the total price of the cart.
     *
     * @param product the product to add
     */
    public void addProductToCart(Product product) {
        product.setQuantity(1);
        cartItems.add(product);
        updateCartPrice();
    }

    /**
     * Updates the quantity of a product in the cart and updates the total price of the cart.
     * If the new quantity is 0, the product is removed from the cart.
     *
     * @param product the product to update
     * @param quantity the new quantity of the product
     */
    public void updateProductQuantity(Product product, int quantity) {
        if (cartItems.contains(product)) {
            int index = cartItems.indexOf(product);
            Product productToUpdate = cartItems.get(index);
            if(quantity != 0) {
                productToUpdate.setQuantity(quantity);
            } else {
                cartItems.remove(productToUpdate);
                System.out.println("Product removed from cart");
            }
            updateCartPrice();
        }
    }

    /**
     * Updates the total price of the products in the cart and notifies the observers.
     */
    private void updateCartPrice() {
        this.totalPrice = 0;
        for (Product product : cartItems) {
            this.totalPrice += product.getPrice() * product.getQuantity();
        }
        notifyCartObservers();
    }

    /**
     * Notifies all observers that the cart has been updated.
     */
    private void notifyCartObservers() {
        for (CartObserver observer : observerList) {
            observer.cartUpdated();
        }
    }

    /**
     * Generates an order summary of the cart, converts it to JSON, and writes it to a file.
     * The file name is "ordersummary.json", but if a file with that name already exists, a number is appended to the file name.
     */
    public void generateOrderSummary() {
        List<OrderSummaryProduct> summaryProducts = cartItems.stream()
                .map(product -> new OrderSummaryProduct(product.getName(), product.getFlavor(), product.getSize(), product.getPrice(), product.getQuantity(), product.getPrice() * product.getQuantity()))
                .toList();

        double totalPrice = cartItems.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();

        OrderSummary orderSummary = new OrderSummary(summaryProducts, totalPrice);

        Gson gson = new Gson();
        var json = gson.toJson(orderSummary);

        String fileName = "ordersummary.json";
        int counter = 1;
        File file = new File(fileName);

        // Check if the file already exists
        while (file.exists()) {
            fileName = "ordersummary" + counter + ".json";
            file = new File(fileName);
            counter++;
        }

        // Write JSON to file
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
            System.out.println("Order summary written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
