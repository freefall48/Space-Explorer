package uc.seng201.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uc.seng201.GameState;
import uc.seng201.SpaceShip;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StateActions {

    public static void saveState(String filename, SpaceShip spaceShip, List<Planet> planets, Planet currentPlanet,
                                    int currentDay, int duration, String shipImage) throws IOException{
        GameState currentState = new GameState(spaceShip, planets, currentPlanet, currentDay, duration, shipImage);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(currentState);
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
