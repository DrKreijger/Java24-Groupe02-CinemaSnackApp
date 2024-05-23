package models;

import be.helha.java24groupe02.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        product = new Product(1, "Test Product", new URL("http://example.com/image.png"), "Vanilla", "Large", 10.0, 10);
    }

    @Test
    void testAddStock() {
        int initialStock = product.getQuantityInStock();
        product.addStock();
        assertEquals(initialStock + 1, product.getQuantityInStock(), "Stock should increase by 1");
    }

    @Test
    public void testRemoveStock() {
        int initialStock = product.getQuantityInStock();
        product.removeStock();
        assertEquals(initialStock - 1, product.getQuantityInStock(), "Stock should decrease by 1");
    }

    @Test
    public void testRemoveStockNotBelowZero() {
        product.setQuantityInStock(0);
        product.removeStock();
        assertEquals(0, product.getQuantityInStock(), "Stock should not go below 0");
    }
}
