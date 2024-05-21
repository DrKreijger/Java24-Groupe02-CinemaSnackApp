package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;

import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectSocket objectSocket = new ObjectSocket(socket)) {
            String message = (String) objectSocket.read();
            System.out.println("Le client a ajouté à son panier un " + message);
            objectSocket.write(message + " ajouté au panier, il en reste " + " en stock");
        } catch (Exception e) {
            System.err.println("Error during communication with the client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}