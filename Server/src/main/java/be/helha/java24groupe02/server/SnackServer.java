package be.helha.java24groupe02.server;

import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import be.helha.java24groupe02.models.exceptions.ProductLoadingException;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class SnackServer {
    private static final int PORT = 3001;
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static final ProductDB productDB = new ProductDB();

    /**
     * The main method of the SnackServer class. This method is responsible for starting the server and handling incoming client connections.
     *
     * @param args The command line arguments passed to the program. This parameter is not currently used.
     */
    public static void main(String[] args) {
        // Create a ServerSocket that listens on the specified PORT
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Print a message to the console indicating that the server has started and on which port it is listening
            System.out.println("Serveur démarré sur le port " + PORT);

            // Continuously listen for incoming client connections
            while (true) {
                // Accept an incoming client connection and create a Socket for communication with the client
                Socket clientSocket = serverSocket.accept();
                // Create a new ClientHandler to handle communication with the connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                // Add the new ClientHandler to the list of active clients
                clients.add(clientHandler);
                // Start a new Thread that runs the ClientHandler's run method
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            // Print any IOExceptions that occur to the console
            e.printStackTrace();
        }
    }

    /**
     * The ClientHandler class is a private static class within the SnackServer class.
     * It implements the Runnable interface, allowing instances of this class to be used in a Thread.
     * Each instance of this class is responsible for handling communication with a single client.
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        /**
         * The constructor for the ClientHandler class.
         *
         * @param socket The Socket object representing the connection to the client.
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * The run method is called when the Thread that this ClientHandler is running on is started.
         * It handles the main communication loop with the client.
         */
        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                sendInitialProductList();

                // Main communication loop
                while (true) {
                    String request = (String) in.readObject();
                    if (request.startsWith("ADD_TO_CART")) {
                        String[] parts = request.split(" ");
                        int productId = Integer.parseInt(parts[1]);
                        int clientStock = Integer.parseInt(parts[2]);
                        updateProductStock(productId, clientStock);
                    } else if (request.startsWith("DELETE_SNACK")) {
                        String[] parts = request.split(" ");
                        int productId = Integer.parseInt(parts[1]);
                        int quantityRemoved = Integer.parseInt(parts[2]);
                        updateProductStock(productId, quantityRemoved);
                    } else if (request.startsWith("ADD_SNACK_QUANTITY")) {
                        String[] parts = request.split(" ");
                        int productId = Integer.parseInt(parts[1]);
                        int clientStock = Integer.parseInt(parts[2]);
                        updateProductStock(productId, clientStock);
                    } else if (request.startsWith("REMOVE_SNACK_QUANTITY")) {
                        String[] parts = request.split(" ");
                        int productId = Integer.parseInt(parts[1]);
                        int clientStock = Integer.parseInt(parts[2]);
                        updateProductStock(productId, clientStock);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * This method sends the initial list of products to the client.
         *
         * @throws IOException If an I/O error occurs while sending the product list.
         */
        private void sendInitialProductList() throws IOException {
            try {
                List<Product> products = productDB.getAllProductsFromDatabase();
                out.writeObject(products);
            } catch (ProductLoadingException e) {
                e.printStackTrace();
            }
        }

        /**
         * This method updates the stock of a specific product.
         *
         * @param productId   The ID of the product to update.
         * @param clientStock The new stock value for the product.
         */
        private void updateProductStock(int productId, int clientStock) {
            try {
                // Récupérer le produit depuis la base de données
                Product product = productDB.getProductById(productId);
                if (product != null) {
                    int newStock = clientStock;
                    productDB.updateProductQuantityInStock(productId, newStock);
                    notifyAllClients();
                } else {
                    System.out.println("Le produit avec l'ID " + productId + " n'existe pas dans la base de données.");
                }
            } catch (ProductLoadingException e) {
                e.printStackTrace();
            }
        }

        /**
         * This method notifies all clients of a change in the product list.
         */
        private void notifyAllClients() {
            try {
                List<Product> products = productDB.getAllProductsFromDatabase();
                for (ClientHandler client : clients) {
                    client.out.writeObject(products);
                }
            } catch (IOException | ProductLoadingException e) {
                e.printStackTrace();
            }
        }
    }
}

