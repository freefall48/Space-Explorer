package uc.seng201;

import uc.seng201.destinations.traders.SpaceTraders;
import uc.seng201.errors.InvalidGameState;
import uc.seng201.destinations.Planet;

import java.util.List;

public class GameState {

    // WARNING: THESE VARIABLES ARE MUTABLE
    private SpaceShip spaceShip;
    private List<Planet> planets;
    private SpaceTraders traders;

    private Planet currentPlanet;
    private int currentDay;
    private int duration;

    public GameState(SpaceShip spaceShip, int duration, List<Planet> planets) {
        this(spaceShip, planets, planets.get(0), 1, duration);
    }

    public GameState(SpaceShip spaceShip, List<Planet> planets, Planet currentPlanet, int currentDay, int duration) {
        this.spaceShip = spaceShip;
        this.planets = planets;
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

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentPlanet(Planet currentPlanet) {
        this.currentPlanet = currentPlanet;
    }

    public Planet planetFromName(String name) {
        for (Planet planet : this.planets) {
            if (planet.getPlanetName().equals(name)) {
                return planet;
            }
        }
        return null;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getDuration() {
        return duration;
    }

    public SpaceTraders getTrader() {
        return this.traders;
    }

    public void setTraders(SpaceTraders traders) {
        this.traders = traders;
    }

    public boolean hasNextDay() {
        return this.currentDay + 1 <= this.duration;
    }

    public void nextDay() throws InvalidGameState {
        if (hasNextDay()) {
            this.currentDay += 1;
        } else {
            throw new InvalidGameState();
        }
    }

    public boolean isMissingShipParts() {
        return this.spaceShip.getMissingParts() > 0;
    }

    public boolean isValidState() {
        if (this.spaceShip.getShieldCount() == 0) {
            return false;
        }
        return this.spaceShip.getShipCrew().size() != 0;
    }

}