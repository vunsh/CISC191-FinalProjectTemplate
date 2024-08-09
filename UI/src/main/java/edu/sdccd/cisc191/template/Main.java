package edu.sdccd.cisc191.template;

import java.io.IOException;

/**
 * The {@code Main} class serves as the entry point for the rhythm game application.
 * It starts the server in a separate thread and then launches the JavaFX application.
 */
public class Main {
    /**
     * The main method that starts the server and launches the JavaFX application.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Start the server in a new thread
        new Thread(() -> {
            Server server = new Server();
            try {
                server.start(4444);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Launch the JavaFX application
        RhythmGameApp.main(args);
    }
}
