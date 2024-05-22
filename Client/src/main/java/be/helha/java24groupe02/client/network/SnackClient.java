package be.helha.java24groupe02.client.network;

import be.helha.java24groupe02.models.Product;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * The SnackClient class is responsible for communicating with the server and handling product updates.
 */
public class SnackClient {
    // The server's address
    private static final String SERVER_ADDRESS = "localhost";
    // The server's port
    private static final int SERVER_PORT = 3001;
    // The output stream to the server
    private ObjectOutputStream out;
    // The input stream from the server
    private ObjectInputStream in;
    // The listener for product updates
    private ProductsUpdateListener productsUpdateListener;
    // The socket used to communicate with the server
    private Socket socket;
    // The client thread
    private Thread clientThread;
    // A flag indicating whether the client is running
    private boolean running;

    /**
     * Starts the client and continuously listens for product updates from the server.
     */
    public void start() {
        running = true;
        clientThread = new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // Continuously listen for product updates
                while (running) {
                    List<Product> updatedProducts = (List<Product>) in.readObject();
                    if (productsUpdateListener != null) {
                        productsUpdateListener.onProductsUpdate(updatedProducts);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    e.printStackTrace();
                }
            } finally {
                clientThread.interrupt();
                closeConnections();
                Platform.exit();
                System.exit(0);
            }
        });
        clientThread.start();
    }

    /**
     * Closes the connections to the server.
     */
    public void stop() {
        System.out.println("Stopping client...");
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (clientThread != null) {
                clientThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the output and input streams.
     */
    private void closeConnections() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to add a product to the cart.
     *
     * @param productId   The ID of the product to add.
     * @param clientStock The quantity of the product to add.
     */
    public void addToCart(int productId, int clientStock) {
        sendRequest("ADD_TO_CART " + productId + " " + clientStock);
    }

    /**
     * Sends a request to the server to remove a snack from the cart.
     *
     * @param productId       The ID of the product to remove.
     * @param quantityRemoved The quantity of the product to remove.
     */
    public void deleteSnackFromCart(int productId, int quantityRemoved) {
        sendRequest("DELETE_SNACK " + productId + " " + quantityRemoved);
    }

    /**
     * Sends a request to the server to increase the quantity of a snack.
     *
     * @param productId   The ID of the product to increase the quantity of.
     * @param clientStock The quantity to increase by.
     */
    public void addSnackQuantity(int productId, int clientStock) {
        sendRequest("ADD_SNACK_QUANTITY " + productId + " " + clientStock);
    }

    /**
     * Sends a request to the server to decrease the quantity of a snack.
     *
     * @param productId   The ID of the product to decrease the quantity of.
     * @param clientStock The quantity to decrease by.
     */
    public void removeSnackQuantity(int productId, int clientStock) {
        sendRequest("REMOVE_SNACK_QUANTITY " + productId + " " + clientStock);
    }

    /**
     * Sends a request to the server.
     *
     * @param request The request to send.
     */
    private void sendRequest(String request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the listener for product updates.
     *
     * @param listener The listener to set.
     */
    public void setProductsUpdateListener(ProductsUpdateListener listener) {
        this.productsUpdateListener = listener;
    }

    /**
     * The interface for a listener that handles product updates.
     */
    public interface ProductsUpdateListener {
        /**
         * Called when the products are updated.
         *
         * @param updatedProducts The updated products.
         */
        void onProductsUpdate(List<Product> updatedProducts);
    }
}

