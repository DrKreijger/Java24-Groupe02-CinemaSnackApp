package be.helha.java24groupe02.common.network;

import java.io.*;
import java.net.Socket;

public class ObjectSocket implements AutoCloseable {
    private final ObjectOutputStream objectOutputStream;
    private final Socket socket;
    private final ObjectInputStream objectInputStream;

    public ObjectSocket(Socket socket) throws IOException {
        this.socket = socket;
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        this.objectOutputStream = new ObjectOutputStream(out);
        this.objectInputStream = new ObjectInputStream(in);
    }

    public void write(Object object) throws IOException {
        objectOutputStream.reset();
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    public Object read() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    @Override
    public void close() throws Exception {
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }
}