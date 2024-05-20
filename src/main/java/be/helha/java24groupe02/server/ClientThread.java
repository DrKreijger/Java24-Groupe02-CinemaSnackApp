package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;

import java.net.Socket;

public class ClientThread extends Thread{
    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try (ObjectSocket objectSocket = new ObjectSocket(socket)) {
            String string = (String) objectSocket.read();
            System.out.println("Received string : " + string);
            objectSocket.write(string.toUpperCase());
        } catch (Exception e) {
            System.err.println("Erreur de communication avec le client");
        }
    }
}
