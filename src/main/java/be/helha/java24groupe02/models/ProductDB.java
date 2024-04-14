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
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setImagePath(resultSet.getString("image_path"));
                product.setCategory(resultSet.getString("category"));
                product.setFlavor(resultSet.getString("flavor"));
                product.setSize(resultSet.getString("size"));
                product.setPrice(resultSet.getDouble("price"));
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
}
