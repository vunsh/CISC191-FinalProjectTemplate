package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 4444);

            String midiFilePath = MidiUtils.loadMidiFile("/twinkle-twinkle-little-star.mid");

            MidiInterpreter midiInterpreter = new MidiInterpreter();
            List<MidiInterpreter.NoteEvent> noteEvents = midiInterpreter.parseMidiFile(midiFilePath);
            ListIterator<MidiInterpreter.NoteEvent> noteIterator = noteEvents.listIterator();

            MidiUtils.playMidiFile(midiFilePath, currentTimeInSeconds -> {
                while (noteIterator.hasNext()) {
                    MidiInterpreter.NoteEvent noteEvent = noteIterator.next();
                    if (noteEvent.timeInSeconds <= currentTimeInSeconds) {
                        System.out.println(noteEvent);
                    } else {
                        noteIterator.previous(); // Move the iterator back if the note is in the future
                        break;
                    }
                }
            });

            // The client process can stop after the MIDI is done playing
            client.stopConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
