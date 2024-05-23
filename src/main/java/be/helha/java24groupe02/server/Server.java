package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ServerConstants;
import be.helha.java24groupe02.models.ProductDB;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ClientThread> clientThreads = new ArrayList<>();
    private static ProductDB productDB = new ProductDB();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
            System.out.println("Server started on port " + ServerConstants.PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                ClientThread clientThread = new ClientThread(socket, productDB);
                clientThreads.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error while starting server");
            e.printStackTrace();
        }
    }

    public static void notifyClients(String message) {
        for (ClientThread clientThread : clientThreads) {
            clientThread.sendMessage(message);
        }
    }
}
