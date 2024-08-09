package edu.sdccd.cisc191.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The {@code FallingBlock} class represents a block that falls down the screen in the rhythm game.
 * The block is associated with a musical note and moves based on the tempo of the music.
 */
public class FallingBlock {
    private double startTime;
    private double y;
    private double speed;
    private int note;
    private int row;
    private boolean started;
    private double canvasHeight;
    private Color color;

    private static final double BLOCK_HEIGHT = 20;
    private static final double BLOCK_WIDTH = 40;

    /**
     * Constructs a {@code FallingBlock} with the specified parameters.
     *
     * @param startTime    the time at which the block starts falling
     * @param y            the initial y-coordinate of the block
     * @param speed        the speed at which the block falls
     * @param note         the musical note associated with the block
     * @param canvasHeight the height of the canvas on which the block is drawn
     * @param row          the row in which the block appears
     * @param color        the color of the block
     */
    public FallingBlock(double startTime, double y, double speed, int note, double canvasHeight, int row, Color color) {
        this.startTime = startTime;
        this.y = y;
        this.speed = speed;
        this.note = note;
        this.canvasHeight = canvasHeight;
        this.row = row;
        this.started = false;
        this.color = color;
    }

    /**
     * Returns the current y-coordinate of the block.
     *
     * @return the y-coordinate of the block
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the row in which the block appears.
     *
     * @return the row index of the block
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the speed at which the block falls.
     *
     * @param speed the speed of the block
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Updates the position of the block based on the elapsed time.
     * The block starts moving only after the current time exceeds the start time.
     *
     * @param elapsedTime the time elapsed since the last update
     * @param currentTime the current time in the game
     */
    public void update(double elapsedTime, double currentTime) {
        if (currentTime >= startTime) {
            started = true;
        }
        if (started) {
            y += speed * elapsedTime;
        }
    }

    /**
     * Draws the block on the canvas at its current position.
     *
     * @param gc           the {@code GraphicsContext} used to draw the block
     * @param canvasWidth  the width of the canvas
     * @param canvasHeight the height of the canvas
     */
    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        gc.setFill(color);
        double xPosition = (canvasWidth / 2) - (BLOCK_WIDTH / 2);
        gc.fillRoundRect(xPosition, y, BLOCK_WIDTH, BLOCK_HEIGHT, 10, 10);
    }

    /**
     * Checks if the block has moved off the screen.
     *
     * @return {@code true} if the block is off the screen, {@code false} otherwise
     */
    public boolean isOffScreen() {
        return y > canvasHeight;
    }

    /**
     * Handles the logic when the block is hit by the player.
     * (This method should be implemented with the specific game logic.)
     */
    public void handleHit() {
        // Logic to handle what happens when the block is hit
    }
}
