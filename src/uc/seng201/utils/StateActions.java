package uc.seng201.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uc.seng201.GameState;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the ability to load or save the current state of the game.
 * 
 * @author Matthew Johnson
 *
 */
public class StateActions {

	/**
	 * Save the game to the file-system. The game state is saved to a JSON file.
	 * 
	 * @param gameState state to be saved to file.
	 * @param filename URI to save the state to.
	 * @throws IOException if unable to write to the file-system.
	 */
    public static void saveState(GameState gameState, String filename) throws IOException{

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String json = gson.toJson(gameState);
        Path file = Paths.get(filename + ".json");
        Files.writeString(file, json, Charset.forName("UTF-8"));
    }

    /**
     * Loads an existing game from a given file. The JSON file is parsed an converted into a valid game state
     * which is then returned.
     * 
     * @param filename URI to load the save from.
     * @return a game state.
     * @throws IOException if unable to read from the file-system.
     */
    public static GameState loadState(String filename) throws IOException {
            Path file = Paths.get(filename);
            String json = Files.readString(file);
            Gson gson = new Gson();
            return gson.fromJson(json, GameState.class);
    }
}
