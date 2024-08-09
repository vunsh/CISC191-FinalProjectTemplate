package edu.sdccd.cisc191.template;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.sdccd.cisc191.template.MidiInterpreter;
import edu.sdccd.cisc191.template.MidiInterpreter.NoteEvent;
import edu.sdccd.cisc191.template.MidiUtils;
import edu.sdccd.cisc191.template.MusicPlaylist;
import edu.sdccd.cisc191.template.MyLinkedList;
import edu.sdccd.cisc191.template.SongMenu;
import edu.sdccd.cisc191.template.MusicPlaylistApp;

import java.util.List;

public class CommonTests {

    // MusicPlaylist Tests
    private MusicPlaylist playlist;

    @Before
    public void setUpPlaylist() {
        playlist = new MusicPlaylist();
    }

    @Test
    public void testAddSongIncreasesSize() {
        int initialSize = playlist.size();
        playlist.addSong("Song 1");
        assertEquals(initialSize + 1, playlist.size());
    }

    @Test
    public void testRemoveSongDecreasesSize() {
        playlist.addSong("Song 1");
        int initialSize = playlist.size();
        playlist.removeSong("Song 1");
        assertEquals(initialSize - 1, playlist.size());
    }

    @Test
    public void testClearPlaylistEmptiesList() {
        playlist.addSong("Song 1");
        playlist.clearPlaylist();
        assertEquals(0, playlist.size());
    }

    // MyLinkedList Tests
    private MyLinkedList<String> myLinkedList;

    @Before
    public void setUpMyLinkedList() {
        myLinkedList = new MyLinkedList<>();
    }

    @Test
    public void testMyLinkedListAddIncreasesSize() {
        int initialSize = myLinkedList.size();
        myLinkedList.add("Item 1");
        assertEquals(initialSize + 1, myLinkedList.size());
    }

    @Test
    public void testMyLinkedListRemoveDecreasesSize() {
        myLinkedList.add("Item 1");
        int initialSize = myLinkedList.size();
        myLinkedList.remove("Item 1");
        assertEquals(initialSize - 1, myLinkedList.size());
    }

}