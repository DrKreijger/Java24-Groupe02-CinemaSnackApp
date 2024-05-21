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

                // Send initial product list to the client
                sendInitialProductList();

                while (true) {
                    String request = (String) in.readObject();
                    if (request.startsWith("ADD_TO_CART")) {
                        int productId = Integer.parseInt(request.split(" ")[1]);
                        updateProductStock(productId, -1);
                    } else if (request.startsWith("DELETE_SNACK")) {
                        int productId = Integer.parseInt(request.split(" ")[1]);
                        updateProductStock(productId, 1);
                    } else if (request.startsWith("ADD_SNACK_QUANTITY")) {
                        int productId = Integer.parseInt(request.split(" ")[1]);
                        updateProductStock(productId, -1);
                    } else if (request.startsWith("REMOVE_SNACK_QUANTITY")) {
                        int productId = Integer.parseInt(request.split(" ")[1]);
                        updateProductStock(productId, 1);
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

        private void updateProductStock(int productId, int delta) {
            try {
                Product product = productDB.getProductById(productId);
                if (product.getQuantityInStock() + delta >= 0) {
                    product.setQuantityInStock(product.getQuantityInStock() + delta);
                    productDB.updateProductQuantityInStock(productId, product.getQuantityInStock());
                    notifyAllClients();
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

