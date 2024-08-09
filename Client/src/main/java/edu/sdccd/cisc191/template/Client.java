package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The {@code Client} class represents a client that connects to a server,
 * sends requests, and receives responses.
 */
public class Client {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private ObjectMapper objectMapper;

    /**
     * Constructs a {@code Client} object and connects to the specified server.
     *
     * @param address the server address
     * @param port    the server port
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        objectMapper = new ObjectMapper();
    }

    /**
     * Sends a request to the server.
     *
     * @param request the request object to send
     * @throws IOException if an I/O error occurs when sending the request
     */
    public void sendRequest(Object request) throws IOException {
        String jsonRequest = objectMapper.writeValueAsString(request);
        output.writeUTF(jsonRequest);
    }

    /**
     * Receives a response from the server.
     *
     * @param responseType the class type of the response object
     * @param <T>          the type of the response object
     * @return the response object received from the server
     * @throws IOException if an I/O error occurs when receiving the response
     */
    public <T> T receiveResponse(Class<T> responseType) throws IOException {
        String jsonResponse = input.readUTF();
        return objectMapper.readValue(jsonResponse, responseType);
    }

    /**
     * Closes the client connection to the server.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void close() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
