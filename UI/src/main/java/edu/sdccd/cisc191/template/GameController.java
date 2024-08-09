package edu.sdccd.cisc191.template;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code GameController} class controls the main gameplay of the rhythm game.
 * It handles the initialization of tracks, starting the game, and managing user input.
 */
public class GameController {

    @FXML
    private Button startButton;
    @FXML
    private Button viewCatalogButton;
    @FXML
    private Button musicPlaylistButton;
    @FXML
    private Canvas trackCanvas0;
    @FXML
    private Canvas trackCanvas1;
    @FXML
    private Canvas trackCanvas2;
    @FXML
    private Canvas trackCanvas3;

    @FXML
    private Label scoreLabel;

    private List<TrackController> trackControllers;
    private AnimationTimer gameLoop;
    private boolean midiStarted = false;
    private long startTime;
    private int score = 0;
    private String selectedMidiFile;

    /**
     * Initializes the game controller by setting up the track controllers and key event handling.
     */
    @FXML
    public void initialize() {
        trackControllers = new ArrayList<>();

        // Initialize track controllers for each row
        trackControllers.add(new TrackController(trackCanvas0, 0, Color.RED, calculateSpeed(), trackCanvas0.getHeight()));
        trackControllers.add(new TrackController(trackCanvas1, 1, Color.GREEN, calculateSpeed(), trackCanvas1.getHeight()));
        trackControllers.add(new TrackController(trackCanvas2, 2, Color.YELLOW, calculateSpeed(), trackCanvas2.getHeight()));
        trackControllers.add(new TrackController(trackCanvas3, 3, Color.BLUE, calculateSpeed(), trackCanvas3.getHeight()));

        // Set up key event handling for each track
        trackCanvas0.setFocusTraversable(true);
        trackCanvas0.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

        trackCanvas1.setFocusTraversable(true);
        trackCanvas1.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

        trackCanvas2.setFocusTraversable(true);
        trackCanvas2.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

        trackCanvas3.setFocusTraversable(true);
        trackCanvas3.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    /**
     * Opens the song catalog in a new thread for browsing.
     */
    @FXML
    private void viewCatalog() {
        new Thread(SongMenu::browseCatalog).start();
    }

    /**
     * Opens the music playlist application in a new thread.
     */
    @FXML
    private void musicPlaylist() {
        MusicPlaylistApp musicPlaylistApp = new MusicPlaylistApp();
        new Thread(musicPlaylistApp::start).start();
    }

    /**
     * Starts the game by loading the selected MIDI file and initializing the falling blocks on each track.
     */
    @FXML
    public void startGame() {
        try {
            // Select the MIDI file if it hasn't been selected already
            if (selectedMidiFile == null) {
                selectMidiFile();
            }

            String midiFilePath = MidiUtils.loadMidiFile(selectedMidiFile);

            MidiInterpreter midiInterpreter = new MidiInterpreter();
            List<MidiInterpreter.NoteEvent> noteEvents = midiInterpreter.parseMidiFile(midiFilePath);

            for (MidiInterpreter.NoteEvent noteEvent : noteEvents) {
                TrackController trackController = trackControllers.get(noteEvent.noteGroup);
                trackController.addFallingBlock(new FallingBlock(noteEvent.timeInSeconds, -40, trackController.getSpeed(), noteEvent.note, trackController.getCanvasHeight(), noteEvent.noteGroup, trackController.getColor()));
            }

            gameLoop = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                        startTime = now;
                        return;
                    }
                    double elapsedTime = (now - lastUpdate) / 1_000_000_000.0;
                    double currentTime = (now - startTime) / 1_000_000_000.0;

                    for (TrackController trackController : trackControllers) {
                        trackController.update(elapsedTime, currentTime);
                        trackController.draw(); // Ensure drawing is happening within the game loop
                    }

                    if (!midiStarted && firstRowHitBar()) {
                        midiStarted = true;
                        MidiUtils.playMidiFile(midiFilePath, currentTimeInSeconds -> {});
                    }

                    lastUpdate = now;
                }
            };
            gameLoop.start();
            trackCanvas0.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to select a MIDI file from the available files in the resources folder.
     */
    private void selectMidiFile() {
        List<String> choices = new ArrayList<>();
        try {
            choices = getMidiFilesFromResources();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        if (choices.isEmpty()) {
            System.out.println("No MIDI files found in resources.");
            return;
        }

        // Create a dialog to select a MIDI file
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Select MIDI File");
        dialog.setHeaderText("Choose a MIDI file to play:");
        dialog.setContentText("Available files:");

        // Get the user's choice
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedFile -> selectedMidiFile = selectedFile);
    }

    /**
     * Retrieves a list of MIDI files from the resources folder.
     *
     * @return a list of MIDI file names
     * @throws IOException        if an I/O error occurs while accessing the files
     * @throws URISyntaxException if the resource path is invalid
     */
    private List<String> getMidiFilesFromResources() throws IOException, URISyntaxException {
        // Get the path to the resources folder
        Path resourcePath = Paths.get(getClass().getClassLoader().getResource("").toURI());

        // Filter and collect MIDI file names
        return Files.walk(resourcePath)
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(fileName -> fileName.endsWith(".mid"))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the speed at which blocks should fall.
     * (Currently returns a default value of 100.0, but can be modified to be dynamic.)
     *
     * @return the speed of the falling blocks
     */
    private double calculateSpeed() {
        return 100.0;
    }

    /**
     * Checks if any block has hit the first row's hit bar.
     *
     * @return {@code true} if a block has hit the first row's hit bar, {@code false} otherwise
     */
    private boolean firstRowHitBar() {
        for (TrackController trackController : trackControllers) {
            if (trackController.isFirstRowHit()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the score displayed on the screen.
     */
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Handles key press events and updates the game state accordingly.
     *
     * @param event the key event triggered by the user
     */
    private void handleKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        int scoreIncrement = 0;

        switch (key) {
            case A:
                scoreIncrement = trackControllers.get(0).handleKeyPress(event, key);
                break;
            case S:
                scoreIncrement = trackControllers.get(1).handleKeyPress(event, key);
                break;
            case D:
                scoreIncrement = trackControllers.get(2).handleKeyPress(event, key);
                break;
            case F:
                scoreIncrement = trackControllers.get(3).handleKeyPress(event, key);
                break;
            default:
                System.out.println("Key not mapped: " + key);
                break;
        }

        score += scoreIncrement;
        updateScore();
    }
}
