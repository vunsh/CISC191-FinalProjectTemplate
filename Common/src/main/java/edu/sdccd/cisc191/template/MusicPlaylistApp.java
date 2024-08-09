package edu.sdccd.cisc191.template;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The {@code MusicPlaylistApp} class provides a console-based application for managing a music playlist.
 * It allows users to add, browse, and remove songs, as well as play the next song in the playlist.
 */
public class MusicPlaylistApp {

    private MusicPlaylist playlist;
    private Scanner scanner;

    /**
     * Constructs a {@code MusicPlaylistApp} object and initializes the music playlist and scanner.
     */
    public MusicPlaylistApp() {
        playlist = new MusicPlaylist(); // Ensure this is properly initialized
        scanner = new Scanner(System.in); // Ensure the scanner is properly initialized
    }

    /**
     * Starts the playlist management application, displaying a menu and processing user input.
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Music Playlist Menu ---");
            System.out.println("1. Add a song");
            System.out.println("2. Browse playlist");
            System.out.println("3. Remove a song");
            System.out.println("4. Play next song");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        addSong();
                        break;
                    case 2:
                        browsePlaylist();
                        break;
                    case 3:
                        removeSong();
                        break;
                    case 4:
                        playNextSong();
                        break;
                    case 5:
                        running = false;
                        System.out.println("Exiting the playlist menu. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.nextLine();  // Clear the invalid input
            }
        }
    }

    /**
     * Prompts the user to enter the name of a song to add to the playlist.
     */
    private void addSong() {
        System.out.print("Enter the name of the song to add: ");
        String song = scanner.nextLine();
        playlist.addSong(song);
        System.out.println("Song added: " + song);
    }

    /**
     * Displays the current playlist.
     */
    private void browsePlaylist() {
        playlist.displayPlaylist();
    }

    /**
     * Prompts the user to enter the name of a song to remove from the playlist.
     */
    private void removeSong() {
        System.out.print("Enter the name of the song to remove: ");
        String song = scanner.nextLine();
        playlist.removeSong(song);
        System.out.println("Song removed: " + song);
    }

    /**
     * Plays the next song in the playlist.
     * If the playlist is empty, a message is displayed.
     */
    private void playNextSong() {
        String nextSong = playlist.playNext();
        if (nextSong != null) {
            System.out.println("Now playing: " + nextSong);
        } else {
            System.out.println("The playlist is empty.");
        }
    }

    /**
     * The main method to run the music playlist application.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        MusicPlaylistApp app = new MusicPlaylistApp();
        app.start();
    }
}
