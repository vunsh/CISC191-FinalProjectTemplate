package edu.sdccd.cisc191.template;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The {@code TrackController} class manages the falling blocks within a specific track of the rhythm game.
 * It handles updating block positions, drawing them on the canvas, and detecting user input for hits.
 */
public class TrackController {
    private Canvas trackCanvas;
    private List<FallingBlock> fallingBlocks;
    private double speed;
    private double canvasHeight;
    private Color color;
    private int row;
    private boolean flashHole = false;  // To manage the visual cue
    private long flashStartTime;

    private static final double HOLE_HEIGHT = 20;
    private static final double HOLE_WIDTH = 40;

    /**
     * Constructs a new {@code TrackController} for a specific track.
     *
     * @param trackCanvas  the canvas associated with this track
     * @param row          the row index for this track
     * @param color        the color associated with this track
     * @param speed        the speed at which blocks fall in this track
     * @param canvasHeight the height of the canvas
     */
    public TrackController(Canvas trackCanvas, int row, Color color, double speed, double canvasHeight) {
        this.trackCanvas = trackCanvas;
        this.fallingBlocks = new ArrayList<>();
        this.speed = speed;
        this.canvasHeight = canvasHeight;
        this.color = color;
        this.row = row;
    }

    /**
     * Returns the speed of the falling blocks in this track.
     *
     * @return the speed of the blocks
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns the height of the canvas.
     *
     * @return the canvas height
     */
    public double getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Returns the color associated with this track.
     *
     * @return the color of the track
     */
    public Color getColor() {
        return color;
    }

    /**
     * Adds a falling block to this track.
     *
     * @param block the falling block to be added
     */
    public void addFallingBlock(FallingBlock block) {
        fallingBlocks.add(block);
    }

    /**
     * Updates the positions of the falling blocks and removes any that have fallen off the screen.
     *
     * @param elapsedTime the elapsed time since the last update
     * @param currentTime the current time in seconds
     */
    void update(double elapsedTime, double currentTime) {
        Iterator<FallingBlock> iterator = fallingBlocks.iterator();
        while (iterator.hasNext()) {
            FallingBlock block = iterator.next();
            block.update(elapsedTime, currentTime);
            if (block.isOffScreen()) {
                iterator.remove();
            }
        }
        if (flashHole && System.currentTimeMillis() - flashStartTime > 200) { // Flash duration of 200ms
            flashHole = false;
        }
    }

    /**
     * Draws the track, including the holes and the falling blocks.
     */
    public void draw() {
        GraphicsContext gc = trackCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, trackCanvas.getWidth(), trackCanvas.getHeight());

        drawHoles(); // Draw the holes first, so blocks pass over them

        for (FallingBlock block : fallingBlocks) {
            block.draw(gc, trackCanvas.getWidth(), trackCanvas.getHeight());
        }
    }

    /**
     * Draws the holes on the track where blocks can be hit.
     */
    public void drawHoles() {
        GraphicsContext gc = trackCanvas.getGraphicsContext2D();
        gc.setFill(flashHole ? color.brighter() : color);
        double yPosition = trackCanvas.getHeight() - HOLE_HEIGHT;
        double xPosition = (trackCanvas.getWidth() / 2) - (HOLE_WIDTH / 2);
        gc.fillRoundRect(xPosition, yPosition, HOLE_WIDTH, HOLE_HEIGHT, 10, 10);
    }

    /**
     * Handles key press events to check if a block was hit and updates the score accordingly.
     *
     * @param event the key event triggered by the user
     * @param key   the key code of the pressed key
     * @return the score increment based on the accuracy of the hit
     */
    public int handleKeyPress(KeyEvent event, KeyCode key) {
        flashHole = true; // Flash the hole whether the block is hit or not
        flashStartTime = System.currentTimeMillis();
        int scoreIncrement = 0;

        double hitTolerance = 50;
        double holePositionY = trackCanvas.getHeight() - HOLE_HEIGHT;
        boolean hitDetected = false;

        for (FallingBlock block : fallingBlocks) {
            if (block.getRow() == row && Math.abs(block.getY() - holePositionY) <= hitTolerance) {
                hitDetected = true;
                scoreIncrement = calculateScore(block.getY(), holePositionY);
                break;
            }
        }
        return scoreIncrement;
    }

    /**
     * Calculates the score based on the distance between the block and the hit bar.
     *
     * @param blockY the Y position of the block
     * @param barY   the Y position of the hit bar
     * @return the score increment based on the proximity to the hit bar
     */
    private int calculateScore(double blockY, double barY) {
        double distance = Math.abs(blockY - barY);
        if (distance < 10) {
            return 100;
        } else if (distance < 20) {
            return 50;
        } else if (distance < 30) {
            return 20;
        } else {
            return 0;
        }
    }

    /**
     * Checks if any block has hit the first row's hit bar.
     *
     * @return {@code true} if a block has hit the first row's hit bar, {@code false} otherwise
     */
    public boolean isFirstRowHit() {
        double holePositionY = trackCanvas.getHeight() - HOLE_HEIGHT * 2.5;
        for (FallingBlock block : fallingBlocks) {
            if (block.getY() >= holePositionY) {
                return true;
            }
        }
        return false;
    }
}
