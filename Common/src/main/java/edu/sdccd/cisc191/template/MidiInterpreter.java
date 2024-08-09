package edu.sdccd.cisc191.template;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

/**
 * The {@code MidiInterpreter} class is responsible for parsing MIDI files and extracting note events.
 */
public class MidiInterpreter {

    /**
     * Represents a note event extracted from a MIDI file.
     */
    public static class NoteEvent {
        public final int note;
        public final long timestamp;
        public final double timeInSeconds;
        public final int noteGroup;

        /**
         * Constructs a {@code NoteEvent} with the specified note, timestamp, time in seconds, and note group.
         *
         * @param note          the MIDI note number
         * @param timestamp     the tick at which the event occurs
         * @param timeInSeconds the time in seconds at which the event occurs
         * @param noteGroup     the group to which the note belongs (used for visualization)
         */
        public NoteEvent(int note, long timestamp, double timeInSeconds, int noteGroup) {
            this.note = note;
            this.timestamp = timestamp;
            this.timeInSeconds = timeInSeconds;
            this.noteGroup = noteGroup;
        }

        @Override
        public String toString() {
            return "NoteEvent{" +
                    "note=" + note +
                    ", timestamp=" + timestamp +
                    ", timeInSeconds=" + timeInSeconds +
                    ", noteGroup=" + noteGroup +
                    '}';
        }
    }

    /**
     * Parses a MIDI file and returns a list of {@code NoteEvent} objects representing the notes in the file.
     *
     * @param filePath the path to the MIDI file
     * @return a list of {@code NoteEvent} objects
     * @throws Exception if an error occurs while reading the file
     */
    public List<NoteEvent> parseMidiFile(String filePath) throws Exception {
        Map<Integer, List<NoteEvent>> noteEventsMap = new HashMap<>();
        List<Integer> notes = new ArrayList<>();
        Sequence sequence = MidiSystem.getSequence(new File(filePath));
        int resolution = sequence.getResolution();
        double tempo = 500000; // default 120 BPM (500,000 microseconds per quarter note)

        // First pass: collect all note values to calculate ranges
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    if (metaMessage.getType() == 0x51) { // Tempo change
                        byte[] data = metaMessage.getData();
                        tempo = ((data[0] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[2] & 0xff);
                    }
                } else if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                        notes.add(sm.getData1());
                    }
                }
            }
        }

        // Calculate dynamic ranges
        OptionalInt minNote = notes.stream().mapToInt(v -> v).min();
        OptionalInt maxNote = notes.stream().mapToInt(v -> v).max();

        if (minNote.isPresent() && maxNote.isPresent()) {
            int range = maxNote.getAsInt() - minNote.getAsInt();
            int groupSize = (range + 3) / 4; // Divide into 4 groups, making sure we cover all rows

            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                            int note = sm.getData1();
                            long tick = event.getTick();
                            double timeInSeconds = (tick * tempo) / (resolution * 1000000.0);
                            int noteGroup = (note - minNote.getAsInt()) / groupSize;
                            if (noteGroup > 3) noteGroup = 3; // Ensure we don't exceed the number of rows

                            NoteEvent noteEvent = new NoteEvent(note, tick, timeInSeconds, noteGroup);

                            // Add the NoteEvent to the HashMap
                            noteEventsMap.computeIfAbsent(note, k -> new ArrayList<>()).add(noteEvent);
                        }
                    }
                }
            }
        }

        // Convert HashMap values to a single list
        List<NoteEvent> noteEvents = new ArrayList<>();
        for (List<NoteEvent> events : noteEventsMap.values()) {
            noteEvents.addAll(events);
        }

        return noteEvents;
    }
}
