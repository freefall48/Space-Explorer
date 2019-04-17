package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.targets.Planet;

import java.util.List;

public class GameState {
    private SpaceShip spaceShip;
    private List<Planet> planets;

    private CrewMember currentActingMember;
    private Planet currentPlanet;
    private int currentDay;
    private int duration;

    public GameState(SpaceShip spaceShip, List<Planet> planets, CrewMember currentActingMember, Planet currentPlanet, int currentDay, int duration) {
        this.spaceShip = spaceShip;
        this.planets = planets;
        this.currentActingMember = currentActingMember;
        this.currentPlanet = currentPlanet;
        this.currentDay = currentDay;
        this.duration = duration;

    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public CrewMember getCurrentActingMember() {
        return currentActingMember;
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getDuration() {
        return duration;
    }
}