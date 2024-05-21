package be.helha.java24groupe02.client.network;

import be.helha.java24groupe02.models.Product;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.function.Consumer;

public class SnackClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 3001;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ProductsUpdateListener productsUpdateListener;

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                List<Product> updatedProducts = (List<Product>) in.readObject();
                if (productsUpdateListener != null) {
                    productsUpdateListener.onProductsUpdate(updatedProducts);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(int productId, int clientStock) {
            sendRequest("ADD_TO_CART " + productId + " " + clientStock);
    }

    public void deleteSnackFromCart(int productId, int quantityRemoved) {
        sendRequest("DELETE_SNACK " + productId + " " + quantityRemoved);
    }

    public void addSnackQuantity(int productId, int clientStock) {
        sendRequest("ADD_SNACK_QUANTITY " + productId + " " + clientStock);
    }

    public void removeSnackQuantity(int productId, int clientStock) {
        sendRequest("REMOVE_SNACK_QUANTITY " + productId + " " + clientStock);
    }

    private void sendRequest(String request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductsUpdateListener(ProductsUpdateListener listener) {
        this.productsUpdateListener = listener;
    }

    public interface ProductsUpdateListener {
        void onProductsUpdate(List<Product> updatedProducts);
    }
}

