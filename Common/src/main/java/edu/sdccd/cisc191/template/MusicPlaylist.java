package edu.sdccd.cisc191.template;

import java.io.*;

/**
 * The {@code MusicPlaylist} class manages a playlist of songs using a custom linked list.
 * It supports adding, removing, and playing songs, as well as saving and loading the playlist
 * from a file.
 */
public class MusicPlaylist {
    private MyLinkedList<String> playlist;
    private static final String FILE_NAME = "playlist.dat";

    /**
     * Constructs a {@code MusicPlaylist} object and loads the playlist from a file if it exists.
     * If no existing playlist is found, a new playlist is started.
     */
    public MusicPlaylist() {
        playlist = new MyLinkedList<>();
        loadPlaylist();
    }

    /**
     * Adds a song to the playlist and saves the updated playlist to a file.
     *
     * @param song the song to add
     */
    public void addSong(String song) {
        playlist.add(song);
        savePlaylist();
    }

    /**
     * Removes a song from the playlist and saves the updated playlist to a file.
     *
     * @param song the song to remove
     */
    public void removeSong(String song) {
        playlist.remove(song);
        savePlaylist();
    }

    /**
     * Displays the current playlist. If the playlist is empty, a message is displayed.
     */
    public void displayPlaylist() {
        if (playlist.size() == 0) {
            System.out.println("The playlist is empty.");
        } else {
            System.out.println("Current Playlist:");
            playlist.display();
        }
    }

    /**
     * Plays and removes the next song from the playlist.
     *
     * @return the next song in the playlist, or {@code null} if the playlist is empty
     */
    public String playNext() {
        return playlist.pollFirst(); // Retrieves and removes the first song
    }

    /**
     * Plays the previous song by adding the last played song back to the start of the playlist.
     *
     * @param lastPlayed the last played song
     * @return the song that was just added to the start of the playlist
     */
    public String playPrevious(String lastPlayed) {
        playlist.add(lastPlayed); // Add the last played song back to the start
        return playlist.peekFirst(); // Peek at the first song
    }

    /**
     * Saves the playlist to a file.
     */
    private void savePlaylist() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(playlist);
        } catch (IOException e) {
            System.err.println("Error saving playlist: " + e.getMessage());
        }
    }

    /**
     * Loads the playlist from a file. If the file does not exist, a new playlist is started.
     */
    private void loadPlaylist() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            playlist = (MyLinkedList<String>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing playlist found. Starting a new one.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading playlist: " + e.getMessage());
        }
    }

    /**
     * Returns the number of songs in the playlist.
     *
     * @return the size of the playlist
     */
    public int size() {
        return playlist.size();
    }

    /**
     * Clears all songs from the playlist and saves the empty playlist to a file.
     */
    public void clearPlaylist() {
        playlist.clear();
        savePlaylist();
    }
}
