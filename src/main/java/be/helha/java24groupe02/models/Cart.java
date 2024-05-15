package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.FileWriter;
import java.io.IOException;

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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void addProductToCart(Product product) {
        product.setQuantity(1);
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
        notifyCartObservers();
    }

    private void notifyCartObservers() {
        for (CartObserver observer : observerList) {
            observer.cartUpdated();
        }
    }

    public void generateOrderSummary() {
        // Crée un objet représentant le résumé de la commande
        OrderSummary orderSummary = new OrderSummary(cartItems, totalPrice);

        // Convertit le résumé de commande en JSON
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String json = gson.toJson(orderSummary);

        // Écrit le JSON dans un fichier
        try (FileWriter fileWriter = new FileWriter("order_summary.json")) {
            fileWriter.write(json);
            System.out.println("Résumé de la commande écrit dans le fichier ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interne pour représenter le résumé de commande
    private static class OrderSummary {
        @Expose
        private List<String> products;
        @Expose
        private double totalPrice;

        public OrderSummary(List<Product> products, double totalPrice) {
            this.products = new ArrayList<>();
            for (Product product : products) {
                this.products.add(product.getSummary());
            }
            this.totalPrice = totalPrice;
        }
        // Getters pour products et totalPrice
    }
}
