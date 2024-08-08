package edu.sdccd.cisc191.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.sound.midi.*;

public class MidiUtils {
    public static String loadMidiFile(String resourcePath) throws Exception {
        InputStream midiStream = MidiUtils.class.getResourceAsStream(resourcePath);
        if (midiStream == null) {
            throw new Exception("MIDI file not found!");
        }
        File tempMidiFile = File.createTempFile("tempMidi", ".mid");
        try (FileOutputStream out = new FileOutputStream(tempMidiFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = midiStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempMidiFile.getAbsolutePath();
    }

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

    public interface MidiCallback {
        void onTimeUpdate(double currentTimeInSeconds);
    }
}
