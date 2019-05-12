package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.destinations.traders.SpaceTraders;
import uc.seng201.destinations.Planet;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.IRandomEvent;
import uc.seng201.events.RandomEventType;
import uc.seng201.utils.Helpers;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.util.List;

/**
 * Contains all instances that relate to the current game. This object can be
 * saved and loaded to provide the exact same state.
 */
public class GameState {

    /**
     * Instance of the players spaceship.
     */
    private SpaceShip spaceShip;
    /**
     * All planets that are available to the player.
     */
    private List<Planet> planets;
    /**
     * SpaceTraders instance.
     */
    private SpaceTraders traders;
    /**
     * Current planet that the spaceship is at.
     */
    private Planet currentPlanet;
    /**
     * Current day of the game.
     */
    private int currentDay;
    /**
     * Number of days that game should play through.
     */
    private int duration;
    /**
     * The players current daily score.
     */
    private int score;

    class NewDay implements Observer {

        @Override
        public void onEvent(Object... args) {
            if (!hasNextDay()) {
                SpaceExplorer.eventHandler.notifyObservers(Event.DEFEAT,
                        "On no! It seems you have failed to rebuild your ship in time! Err....");
            }
            if (Helpers.randomGenerator.nextBoolean()) {
                SpaceExplorer.eventHandler.notifyObservers(Event.RANDOM_EVENT, EventTrigger.START_DAY);
            }

            currentDay += 1;
//            nextDayTraders();
            computeScore();
        }

        private void nextDayTraders() {
            boolean isFriendly = false;
            for (CrewMember crewMember : spaceShip.getShipCrew()) {
                if (crewMember.getModifications().contains(Modifications.FRIENDLY)) {
                    isFriendly = true;
                }
            }
            traders.generateAvailableItemsToday(isFriendly);
        }
    }

    /**
     *
     * @param spaceShip players spaceship.
     * @param duration number of days the game should run.
     * @param planets planets to be available to the player.
     */
    public GameState(SpaceShip spaceShip, int duration, List<Planet> planets) {
        this(spaceShip, planets, planets.get(0), 1, duration);
    }

    /**
     * Creates a game state that can be fully customised. Contains no checks
     * to validate the arguments.
     *
     * @param spaceShip players spaceship.
     * @param planets planets to be available to the player.
     * @param currentPlanet planet to start at.
     * @param currentDay day that the player should start on.
     * @param duration number of days the game should run.
     */
    public GameState(SpaceShip spaceShip, List<Planet> planets, Planet currentPlanet, int currentDay, int duration) {
        this();
        this.spaceShip = spaceShip;
        this.planets = planets;
        this.currentPlanet = currentPlanet;
        this.currentDay = currentDay;
        this.duration = duration;
        this.score = 0;

        traders = new SpaceTraders();


    }

    private GameState() {
        SpaceExplorer.eventHandler.addObserver(Event.START_DAY, new NewDay());
    }

    /**
     * Returns the spaceship instance for this game state.
     *
     * @return spaceship.
     */
    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    /**
     * Returns a list of planets that are available for this game state.
     *
     * @return list of planets.
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    /**
     * Returns the planet that the spaceship is currently orbiting.
     *
     * @return planet currently orbiting.
     */
    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    /**
     * Changes the planet that is currently being orbited.
     *
     * @param newPlanet new planet to orbit.
     * @throws IllegalArgumentException if the new planet is not known to the game state.
     */
    public void setCurrentPlanet(Planet newPlanet) throws IllegalArgumentException {
        if (planets.contains(newPlanet)) {
            this.currentPlanet = newPlanet;
        } else {
            throw new IllegalArgumentException("That planet is not a known planet.");
        }
    }

    /**
     * Returns the first planet occurrence that has that name, or null if that planet
     * is not known.
     *
     * @param name name of planet to find.
     * @return planet if present or null if not.
     */
    public Planet planetFromName(String name) {
        for (Planet planet : planets) {
            if (planet.getPlanetName().equals(name)) {
                return planet;
            }
        }
        return null;
    }

    /**
     * Returns the current day of the game state.
     *
     * @return current day.
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * Returns the intended duration of the game.
     *
     * @return duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the space trader instance for this game state.
     *
     * @return space traders instance.
     */
    public SpaceTraders getTrader() {
        return traders;
    }

    /**
     * Returns if the game state can move to a new day.
     *
     * @return true if next day available.
     */
    private boolean hasNextDay() {
        return currentDay + 1 <= duration;
    }

    public boolean isMissingShipParts() {
        return spaceShip.getMissingParts() > 0;
    }

    public void computeScore() {
        score = 0;
        spaceShip.getShipCrew().forEach(crewMember -> score += 100);
        score += spaceShip.getShieldCount() * 500;
        spaceShip.getShipItems().forEach((key, value) -> score += 50);
        score += (spaceShip.getMissingPartsAtStart() - spaceShip.getMissingParts()) * 1000;
        score += (duration - currentDay) * 1000;
        score += spaceShip.getBalance() * 10;
    }

    public int getScore() {
        return score;
    }

}
