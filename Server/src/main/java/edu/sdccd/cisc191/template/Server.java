package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;

/**
 * The {@code Server} class represents a simple multi-threaded server that listens for client connections
 * on a specified port. It handles each client connection in a separate thread using the {@code ClientHandler} class.
 */
public class Server {
    private ServerSocket serverSocket;

    /**
     * Starts the server on the specified port and listens for client connections.
     * For each client connection, a new {@code ClientHandler} thread is started.
     *
     * @param port the port number on which the server will listen for connections
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    /**
     * Stops the server by closing the server socket.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void stop() throws IOException {
        serverSocket.close();
    }

    /**
     * The {@code ClientHandler} class handles communication with a connected client in a separate thread.
     * It reads input from the client and can process it based on the server's logic.
     */
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        /**
         * Constructs a {@code ClientHandler} for the specified client socket.
         *
         * @param socket the client socket
         */
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        /**
         * Runs the client handler thread, reading input from the client and processing it.
         * The connection is closed after the client disconnects.
         */
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    // Add your logic to handle client input
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main method to start the server on port 4444.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
