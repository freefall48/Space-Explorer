package uc.seng201.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uc.seng201.GameState;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StateActions {

    public static boolean saveState(String filename, SpaceShip spaceShip, List<Planet> planets, CrewMember currentActing, Planet currentPlanet, int currentDay, int duration) {
        GameState currentState = new GameState(spaceShip, planets, currentActing, currentPlanet, currentDay, duration);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(currentState);
        try {
            Path file = Paths.get(filename + ".json");
            Files.writeString(file, json, Charset.forName("UTF-8"));
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save game state!");
            return false;
        }
    }

    public static GameState loadState(String filename) {
        try {
            Path file = Paths.get(filename + ".json");
            String json = Files.readString(file);
            Gson gson = new Gson();
            return gson.fromJson(json, GameState.class);
        } catch (IOException e) {
            System.out.println("Failed to load game.");
            return null;
        }
    }
}
