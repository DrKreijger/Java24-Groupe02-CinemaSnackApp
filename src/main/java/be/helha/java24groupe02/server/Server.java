package be.helha.java24groupe02.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args)  {
        System.out.println("Démarrage du serveur...");

        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Serveur demarré sur le port 8000");

            while (true) {
                try {
                    System.out.println("En attente d'une connexion");
                    Socket client = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("Erreur lors de la connexion avec le client");
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la creation du serveur");
            throw new RuntimeException(e);
        }


    }
}
