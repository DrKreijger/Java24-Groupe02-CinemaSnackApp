package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;
import be.helha.java24groupe02.models.ProductDB;

import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final ProductDB productDB;
    private ObjectSocket objectSocket;

    public ClientThread(Socket socket, ProductDB productDB) {
        this.socket = socket;
        this.productDB = productDB;
        try {
            this.objectSocket = new ObjectSocket(socket);
        } catch (IOException e) {
            System.err.println("Error creating ObjectSocket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String message = (String) objectSocket.read();
            System.out.println("Client message: " + message);
            // Handle message, update stock, etc.
            Server.notifyClients(message + " stock updated");

        } catch (Exception e) {
            System.err.println("Error during communication with the client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            if (objectSocket != null) {
                objectSocket.write(message);
            }
        } catch (IOException e) {
            System.err.println("Error sending message to client: " + e.getMessage());
        }
    }
}
