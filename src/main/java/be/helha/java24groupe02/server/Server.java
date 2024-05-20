package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ServerConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
            while (true) {
                System.out.println("Waiting for client...");
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");

                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error while starting server");
            e.printStackTrace();
        }
    }
}
