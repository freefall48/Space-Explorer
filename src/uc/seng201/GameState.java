package uc.seng201;

import uc.seng201.targets.Planet;

import java.util.List;

public class GameState {
    private SpaceShip spaceShip;
    private List<Planet> planets;

    private Planet currentPlanet;
    private int currentDay;
    private int duration;
    private String shipImage;

    public GameState(SpaceShip spaceShip, List<Planet> planets, Planet currentPlanet, int currentDay, int duration,
                     String shipImage) {
        this.spaceShip = spaceShip;
        this.planets = planets;
        this.currentPlanet = currentPlanet;
        this.currentDay = currentDay;
        this.duration = duration;
        this.shipImage = shipImage;

    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public List<Planet> getPlanets() {
        return planets;
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

    public String getShipImage() {return shipImage;}
}