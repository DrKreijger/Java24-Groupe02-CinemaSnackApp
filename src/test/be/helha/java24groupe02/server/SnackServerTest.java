package be.helha.java24groupe02.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import be.helha.java24groupe02.models.Product;
import be.helha.java24groupe02.models.ProductDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SnackServerTest {

    private SnackServer.ClientHandler clientHandler;
    private Socket mockSocket;
    private ObjectOutputStream mockOut;
    private ObjectInputStream mockIn;
    private ProductDB mockProductDB;
    private List<Product> mockProductList;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock the socket and streams
        mockSocket = mock(Socket.class);
        mockOut = mock(ObjectOutputStream.class);
        mockIn = mock(ObjectInputStream.class);

        // Mock the ProductDB and its methods
        mockProductDB = mock(ProductDB.class);

        // Create mock product list with valid URLs
        URL imageUrl = new URL("http://example.com/image.jpg");
        mockProductList = Arrays.asList(
                new Product(1, "Product1", imageUrl, "Sweet", "Small", 1.99, 10),
                new Product(2, "Product2", imageUrl, "Salty", "Medium", 2.99, 20)
        );

        // Inject the mocks into the ClientHandler
        clientHandler = new SnackServer.ClientHandler(mockSocket);
        clientHandler.out = mockOut;
        clientHandler.in = mockIn;

        // Set the static productDB field to the mocked ProductDB
        SnackServer.productDB = mockProductDB;

        // Stub the getAllProductsFromDatabase method
        when(mockProductDB.getAllProductsFromDatabase()).thenReturn(mockProductList);
    }

    @Test
    public void testSendInitialProductList() throws Exception {
        clientHandler.sendInitialProductList();

        // Capture the argument passed to writeObject
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockOut).writeObject(argumentCaptor.capture());

        // Assert that the argument is the expected product list
        assertEquals(mockProductList, argumentCaptor.getValue());
    }

    @Test
    public void testUpdateProductStock() throws Exception {
        // Stub the getProductById method
        URL imageUrl = new URL("http://example.com/image.jpg");
        Product mockProduct = new Product(1, "Product1", imageUrl, "Sweet", "Small", 1.99, 10);
        when(mockProductDB.getProductById(1)).thenReturn(mockProduct);

        // Create a spy of the client handler to verify the call to notifyAllClients
        SnackServer.ClientHandler spyClientHandler = spy(clientHandler);

        // Call the method to test
        spyClientHandler.updateProductStock(1, 15);

        // Verify that updateProductQuantityInStock was called with the correct arguments
        verify(mockProductDB).updateProductQuantityInStock(1, 15);
        // Verify that notifyAllClients was called
        verify(spyClientHandler).notifyAllClients();
    }

    @Test
    public void testNotifyAllClients() throws Exception {
        // Add the client handler to the static clients list
        SnackServer.clients.add(clientHandler);

        // Call the method to test
        clientHandler.notifyAllClients();

        // Capture the argument passed to writeObject
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockOut).writeObject(argumentCaptor.capture());

        // Assert that the argument is the expected product list
        assertEquals(mockProductList, argumentCaptor.getValue());
    }
}