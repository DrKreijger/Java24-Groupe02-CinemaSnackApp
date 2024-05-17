package be.helha.java24groupe02.models;

import be.helha.java24groupe02.models.exceptions.ProductLoadingException;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDB {

    private static final String DATABASE_URL = "jdbc:sqlite:snacks_simple.db";

    public List<Product> getAllProductsFromDatabase() throws ProductLoadingException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                URL imagePath = getClass().getResource(resultSet.getString("image_path"));
                String flavor = resultSet.getString("flavor");
                String size = resultSet.getString("size");
                int quantityInStock = resultSet.getInt("quantity_in_stock");
                Product product = new Product(productId, name, imagePath, flavor, size, price, quantityInStock);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new ProductLoadingException();
        }
        return products;
    }

    public void updateProductQuantityInStock (int productId, int newStock) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Products SET quantity_in_stock = ? WHERE product_id = ?")) {
            statement.setInt(1, newStock);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du stock du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeStockToDefault() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Products SET quantity_in_stock = 10")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation du stock par défaut : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
