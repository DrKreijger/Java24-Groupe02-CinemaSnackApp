package be.helha.java24groupe02.models;

import be.helha.java24groupe02.models.Cart;
import be.helha.java24groupe02.models.CartObserver;
import be.helha.java24groupe02.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    private Cart cart;
    private Product product;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        cart = new Cart();
        product = new Product(1, "Test Product", new URL("http://example.com/image.png"), "Vanilla", "Large", 10.0, 10);
    }

    @Test
    public void testAddObserver() {
        CartObserver observer = Mockito.mock(CartObserver.class);
        cart.addObserver(observer);
        cart.addProductToCart(product);
        Mockito.verify(observer).cartUpdated();
    }

    @Test
    public void testAddProductToCart() {
        cart.addProductToCart(product);
        assertTrue(cart.getCartItems().contains(product), "Product should be added to the cart");
        assertEquals(10.0, cart.getTotalPrice(), "Total price should be updated correctly");
    }

    @Test
    public void testUpdateProductQuantity() {
        cart.addProductToCart(product);
        cart.updateProductQuantity(product, 5);
        assertEquals(5, product.getQuantity(), "Product quantity should be updated");
        assertEquals(50.0, cart.getTotalPrice(), "Total price should be updated correctly");

        cart.updateProductQuantity(product, 0);
        assertTrue(cart.getCartItems().isEmpty(), "Product should be removed from the cart when quantity is 0");
        assertEquals(0.0, cart.getTotalPrice(), "Total price should be 0 after removing the product");
    }

    @Test
    public void testGenerateOrderSummary() {
        cart.addProductToCart(product);
        cart.generateOrderSummary();
        // Add assertions to verify the JSON output if needed
    }

    @Test
    public void testUpdateCartPrice() throws MalformedURLException {
        cart.addProductToCart(product);
        assertEquals(10.0, cart.getTotalPrice(), "Total price should be updated correctly");

        Product product2 = new Product(2, "Another Product", new URL("http://example.com/image2.png"), "Chocolate", "Medium", 15.0, 5);
        cart.addProductToCart(product2);
        assertEquals(25.0, cart.getTotalPrice(), "Total price should be updated correctly with multiple products");
    }

    @Test
    public void testNotifyCartObservers() {
        CartObserver observer = Mockito.mock(CartObserver.class);
        cart.addObserver(observer);
        cart.addProductToCart(product);
        Mockito.verify(observer).cartUpdated();
    }

}