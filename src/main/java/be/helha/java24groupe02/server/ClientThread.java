package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;
import be.helha.java24groupe02.models.Product;

import java.io.Serializable;
import java.net.Socket;

public class ClientThread extends Thread implements Serializable {
    private final Socket socket;
    public Product product;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectSocket objectSocket = new ObjectSocket(socket)) {
            String message = (String) objectSocket.read();
            System.out.println("Le client a ajouté à son panier un " + message);
            int stock = product.quantityInStock;
            objectSocket.write(stock);
            System.out.println("Le stock du produit est de " + stock);
        } catch (Exception e) {
            System.err.println("Error during communication with the client: " + e.getMessage());
        }
    }
}