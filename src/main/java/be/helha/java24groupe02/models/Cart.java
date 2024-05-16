package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        List<OrderSummaryProduct> summaryProducts = cartItems.stream()
                .map(product -> new OrderSummaryProduct(product.getName(), product.getFlavor(), product.getSize(), product.getPrice(), product.getQuantity(), product.getPrice() * product.getQuantity()))
                .toList();

        double totalPrice = cartItems.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();

        OrderSummary orderSummary = new OrderSummary(summaryProducts, totalPrice);

        Gson gson = new Gson();
        var json = gson.toJson(orderSummary);
        
        // Write JSON to file
        try (FileWriter fileWriter = new FileWriter("OrderSummary.json")) {
            fileWriter.write(json);
            System.out.println("Order summary written");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromCart(int productId) {
        // Recherche du produit dans le panier
        Product removedProduct = null;
        for (Product product : cartItems) {
            if (product.getId() == productId) {
                removedProduct = product;
                break;
            }
        }
        // Si le produit est trouvé, on le supprime du panier et on soustrait son prix du prix total
        if (removedProduct != null) {
            cartItems.remove(removedProduct);
            totalPrice -= removedProduct.getPrice();
        }
    }
}
