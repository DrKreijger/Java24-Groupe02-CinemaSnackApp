package be.helha.java24groupe02.server;

import be.helha.java24groupe02.common.network.ObjectSocket;

public class Client extends Thread{
    private final Server server;
    ObjectSocket objectSocket;

    public Client(ObjectSocket objectSocket, Server server) {
        this.objectSocket = objectSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object object = this.objectSocket.read();
                System.out.println("Received object : " + object);
            }
        } catch (Exception e) {
            System.err.println("Error while reading object");
            e.printStackTrace();
        }
    }
}
