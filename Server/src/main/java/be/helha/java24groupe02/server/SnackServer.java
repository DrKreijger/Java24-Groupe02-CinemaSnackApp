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

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                sendInitialProductList();

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

        private void sendInitialProductList() throws IOException {
            try {
                List<Product> products = productDB.getAllProductsFromDatabase();
                out.writeObject(products);
            } catch (ProductLoadingException e) {
                e.printStackTrace();
            }
        }

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

