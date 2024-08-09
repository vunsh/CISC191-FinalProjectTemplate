package edu.sdccd.cisc191.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.sound.midi.*;

/**
 * The {@code MidiUtils} class provides utility methods for handling MIDI files, including loading,
 * playing, and extracting tempo information.
 */
public class MidiUtils {

    /**
     * Loads a MIDI file from the resources directory and returns the file path.
     *
     * @param fileName the name of the MIDI file to load
     * @return the file path of the loaded MIDI file
     * @throws Exception if the MIDI file cannot be found or loaded
     */
    public static String loadMidiFile(String fileName) throws Exception {
        InputStream inputStream = MidiUtils.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new Exception("MIDI file not found: " + fileName);
        }
        // Log to verify the resource loading
        System.out.println("MIDI file loaded: " + fileName);
        return inputStreamToFile(inputStream, fileName);
    }

    /**
     * Plays the specified MIDI file and uses a callback to provide updates on the current time in seconds.
     *
     * @param filePath the path to the MIDI file to play
     * @param callback the callback to receive updates on the current time in seconds
     */
    public static void playMidiFile(String filePath, MidiCallback callback) {
        new Thread(() -> {
            try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                Sequence sequence = MidiSystem.getSequence(new File(filePath));
                sequencer.setSequence(sequence);
                sequencer.start();

                while (sequencer.isRunning()) {
                    double currentTimeInSeconds = sequencer.getMicrosecondPosition() / 1_000_000.0;
                    callback.onTimeUpdate(currentTimeInSeconds);
                    Thread.sleep(10); // Check every 10 milliseconds
                }

                sequencer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Retrieves the BPM (Beats Per Minute) from the specified MIDI file.
     *
     * @param filePath the path to the MIDI file
     * @return the BPM of the MIDI file
     * @throws Exception if the BPM cannot be found or calculated
     */
    public static double getBPM(String filePath) throws Exception {
        File midiFile = new File(filePath);
        Sequence sequence = MidiSystem.getSequence(midiFile);
        Track[] tracks = sequence.getTracks();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    if (metaMessage.getType() == 0x51) { // Set Tempo meta message
                        byte[] data = metaMessage.getData();
                        int mpq = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
                        return 60000000.0 / mpq;
                    }
                }
            }
        }
        throw new Exception("BPM not found in MIDI file!");
    }

    /**
     * Interface for receiving updates on the current time in seconds while playing a MIDI file.
     */
    public interface MidiCallback {
        /**
         * Called to provide an update on the current time in seconds during MIDI playback.
         *
         * @param currentTimeInSeconds the current time in seconds
         */
        void onTimeUpdate(double currentTimeInSeconds);
    }

    /**
     * Converts an InputStream to a temporary file and returns the file path.
     *
     * @param inputStream the InputStream to convert
     * @param fileName    the name of the file to create
     * @return the file path of the created temporary file
     * @throws Exception if an error occurs while creating the file
     */
    private static String inputStreamToFile(InputStream inputStream, String fileName) throws Exception {
        File tempFile = File.createTempFile(fileName, ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile.getAbsolutePath();
    }
}
