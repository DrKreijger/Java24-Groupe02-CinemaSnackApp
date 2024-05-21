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
    private Consumer<List<Product>> productsUpdateListener;

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                List<Product> products = (List<Product>) in.readObject();
                if (productsUpdateListener != null) {
                    productsUpdateListener.accept(products);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(int productId) {
            sendRequest("ADD_TO_CART " + productId);
    }

    public void deleteSnackFromCart(int productId) {
        sendRequest("DELETE_SNACK " + productId);
    }

    public void addSnackQuantity(int productId) {
        sendRequest("ADD_SNACK_QUANTITY " + productId);
    }

    public void removeSnackQuantity(int productId) {
        sendRequest("REMOVE_SNACK_QUANTITY " + productId);
    }

    private void sendRequest(String request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductsUpdateListener(Consumer<List<Product>> listener) {
        this.productsUpdateListener = listener;
    }
}

