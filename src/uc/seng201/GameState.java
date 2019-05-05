package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.CrewAction;
import uc.seng201.crew.modifers.Modifications;
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
        for (Planet planet : planets) {
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
        return traders;
    }

    public void setTraders(SpaceTraders traders) {
        this.traders = traders;
    }

    public boolean hasNextDay() {
        return currentDay + 1 <= duration;
    }

    public void nextDay() throws InvalidGameState {
        if (!hasNextDay()) {
            throw new InvalidGameState();
        }
        currentDay += 1;
        spaceShip.nextDay();

        boolean isFriendly = false;
        for (CrewMember crewMember: spaceShip.getShipCrew()) {
            if (crewMember.getModifications().contains(Modifications.FRIENDLY)) {
                isFriendly = true;
            }
        }
        traders.generateAvailableItemsToday(isFriendly);
    }

    public boolean isMissingShipParts() {
        return spaceShip.getMissingParts() > 0;
    }

}