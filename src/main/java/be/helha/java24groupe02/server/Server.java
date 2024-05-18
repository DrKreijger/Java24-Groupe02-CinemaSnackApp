package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;
import be.helha.java24groupe02.common.network.ServerConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Client> client = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.go();
        } catch (IOException e) {
            System.out.println("Error while starting server");
            e.printStackTrace();
        }
    }

    /**
     * Start the server
     * @throws IOException If the server cannot be started
     */
    private void go() throws IOException {
        System.out.println("Starting server...");

        ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        System.out.println("Server started on port " + ServerConstants.PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            ObjectSocket objectSocket = new ObjectSocket(socket);

            // Create a new thread to handle the client
            Client thread = new Client(objectSocket, this);
            this.client.add(thread);
            thread.start();

        }

    }
}
