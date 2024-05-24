package be.helha.java24groupe02.client.network;

import be.helha.java24groupe02.client.network.SnackClient;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SnackClientTest {

    private static SnackClient client;
    private static Socket mockSocket;
    private static ObjectOutputStream mockOut;
    private static ObjectInputStream mockIn;
    private static SnackClient.ProductsUpdateListener mockListener;

    @BeforeAll
    public static void setUp() throws IOException {
        // Mock the socket and streams
        mockSocket = mock(Socket.class);
        mockOut = mock(ObjectOutputStream.class);
        mockIn = mock(ObjectInputStream.class);

        // Mock the ProductsUpdateListener
        mockListener = mock(SnackClient.ProductsUpdateListener.class);

        // Create SnackClient instance with mock ServerSocket
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(mockSocket);
        client = new SnackClient();
        client.setProductsUpdateListener(mockListener);

        // Start the client
        client.start();
    }

    @AfterAll
    public static void tearDown() {
        // Close the connections and socket
        client.closeConnections();
        try {
            if (mockSocket != null && !mockSocket.isClosed()) {
                mockSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stop the client
        client.stop();
    }

    @Test
    @Order(1)
    public void testStart() {
        // Verify that the client thread is running
        assertTrue(client.isRunning());
    }

    @Test
    @Order(2)
    public void testCloseConnections() throws IOException {
        // Mock the output and input streams to be open
        when(mockSocket.getOutputStream()).thenReturn(mockOut);
        when(mockSocket.getInputStream()).thenReturn(mockIn);

        // Call the closeConnections method
        client.closeConnections();

        // Verify that the output and input streams are closed
        verify(mockOut).close();
        verify(mockIn).close();
    }

    @Test
    @Order(3)
    public void testStop() {
        // Call the stop method
        client.stop();

        // Verify that the client thread is stopped
        assertFalse(client.isRunning());
    }
}
