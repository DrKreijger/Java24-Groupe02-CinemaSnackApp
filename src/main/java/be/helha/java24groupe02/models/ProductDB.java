package be.helha.java24groupe02.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDB {

    private static final String DATABASE_URL = "jdbc:sqlite:snacks_simple.db";

    public List<Product> getAllProductsFromDatabase() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String imagePath = resultSet.getString("image_path");
                String flavor = resultSet.getString("flavor");
                String size = resultSet.getString("size");
                int quantityInStock = resultSet.getInt("quantity_in_stock");
                Product product = new Product(productId, name, price, imagePath, flavor, size, quantityInStock);
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits depuis la base de données : " + e.getMessage());
            e.printStackTrace();
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
}
