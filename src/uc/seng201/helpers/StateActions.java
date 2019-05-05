package uc.seng201.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uc.seng201.GameState;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StateActions {

    public static void saveState(GameState gameState, String filename) throws IOException{

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String json = gson.toJson(gameState);
        Path file = Paths.get(filename + ".json");
        Files.writeString(file, json, Charset.forName("UTF-8"));
    }

    public static GameState loadState(String filename) throws IOException {
            Path file = Paths.get(filename);
            String json = Files.readString(file);
            Gson gson = new Gson();
            return gson.fromJson(json, GameState.class);
    }
}
