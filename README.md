# CISC191 Architect Assignment 2 ~ JavaHero

## Overview
This project is a rhythm game inspired by Guitar Hero, developed for a Computer Science course project. It is currently unfinished as of version 2.0.0 yet is functional and has the ability to theoretically take in any single track MIDI file.

## Prerequisites
1. Maven
2. Git
3. JDK 1.8 or higher

## Building the Project
To build the project, run the following Maven command from the project's root directory:

```bash
mvn clean install
```

This will compile all modules and package the application into JAR files.

## Running the Project
The rhythm game is launched from the `Main` class in the `UI` module, which starts the server and client together. Follow these steps to run the project:

1. **Build the project** using Maven as described above.

2. **Navigate to the UI module's target directory**:

    ```bash
    cd UI/target
    ```

3. **Run the project** using the following command:

    ```bash
    java -jar UI-1.0.0.jar
    ```

This will start the server in a separate thread and launch the game.

## Modules

### Common Module
Contains classes shared between the client and server, such as data structures and utilities.

### Server Module
The server application handles multiple client connections. It is automatically started when running the main game application.

### Client Module
The client connects to the server and is also started automatically when running the main game application.

### UI Module
The UI module contains the main game logic and user interface, implemented using JavaFX. This is where the game is played, and it includes features like song selection, playlist management, and gameplay.

## Features
- **Rhythm Game**: Play along with *any* MIDI file in a Guitar Hero-style interface.
- **MIDI Interpretation**: Converts MIDI files into in-game notes that fall along different tracks.
- **Music Playlists**: Manage a playlist of songs, add and remove songs, and save/load the playlist between sessions.
- **Server-Client Architecture**: The game uses a server-client model to manage connections and game state.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
