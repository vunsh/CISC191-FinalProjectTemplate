package edu.sdccd.cisc191.template;

import java.util.Scanner;

/**
 * The {@code SongMenu} class provides a simple console-based menu for browsing and displaying a catalog of songs.
 * It uses a 2D array to store the song titles and their corresponding difficulty levels.
 */
public class SongMenu {
    private static String[][] songGrid = {
            {"Twinkle Twinkle Little Star", "Easy"},
            {"Wii Channel Music", "Medium"},
            {"Moonlight Sonata", "Hard"}
    };

    /**
     * Starts the song catalog browsing menu, allowing the user to display songs or exit the menu.
     */
    public static void browseCatalog() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Song Catalog ---");
            System.out.println("1. Display Songs");
            System.out.println("2. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displaySongs();
                    break;
                case 2:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the list of songs stored in the {@code songGrid} array, showing the song title and difficulty level.
     */
    private static void displaySongs() {
        System.out.println("\n--- Song List ---");
        for (int i = 0; i < songGrid.length; i++) {
            System.out.printf("%d: %s (%s)\n", i + 1, songGrid[i][0], songGrid[i][1]);
        }
    }
}
