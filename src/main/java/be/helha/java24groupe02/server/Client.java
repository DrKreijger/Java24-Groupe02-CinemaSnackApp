package be.helha.java24groupe02.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.net.SocketException;

public class Client {
    public static void main(String[] args) {
        System.out.println("Démarrage du client");
        try {
            Socket socket = new Socket("localhost", 8000);
            System.out.println("Connexion établie avec le serveur");
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la connexion au serveur");
            throw new RuntimeException(e);
        }
    }
}
