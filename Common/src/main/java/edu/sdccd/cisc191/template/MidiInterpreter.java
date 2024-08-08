package edu.sdccd.cisc191.template;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class MidiInterpreter {
    public static class NoteEvent {
        public final int note;
        public final long timestamp;
        public final double timeInSeconds;
        public final int noteGroup;

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

    public List<NoteEvent> parseMidiFile(String filePath) throws Exception {
        List<NoteEvent> noteEvents = new ArrayList<>();
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
            int groupSize = range / 3; // Divide into 3 groups

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
                            noteEvents.add(new NoteEvent(note, tick, timeInSeconds, noteGroup));
                        }
                    }
                }
            }
        }

        return noteEvents;
    }
}
