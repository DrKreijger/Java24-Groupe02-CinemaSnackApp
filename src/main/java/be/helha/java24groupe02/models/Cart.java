package be.helha.java24groupe02.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> cartItems = new ArrayList<>();
    private double totalPrice;

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
