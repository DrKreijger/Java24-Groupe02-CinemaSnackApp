package be.helha.java24groupe02.client.network;

import be.helha.java24groupe02.models.Product;

import java.io.*;
import java.net.*;
import java.util.List;

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

            // Start a thread to listen for updates from the server
            new Thread(new ServerListener()).start();
            while (true) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    List<Product> products = (List<Product>) in.readObject();
                    if (productsUpdateListener != null) {
                        productsUpdateListener.onProductsUpdate(products);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ProductsUpdateListener {
        void onProductsUpdate(List<Product> products);
    }

    public void setProductsUpdateListener(ProductsUpdateListener productsUpdateListener) {
        this.productsUpdateListener = productsUpdateListener;
    }
}

