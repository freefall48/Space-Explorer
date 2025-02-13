package uc.seng201.environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.misc.Planet;
import uc.seng201.misc.events.EventTrigger;
import uc.seng201.misc.traders.SpaceTraders;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private Set<Planet> planets;
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

    /**
     * @param spaceShip players spaceship.
     * @param duration  number of days the game should run.
     * @param planets   planets to be available to the player.
     */
    public GameState(final SpaceShip spaceShip, final int duration, final Set<Planet> planets) {
        this(spaceShip, planets, null, 1, duration);
    }

    /**
     * Creates a game state that can be fully customised. Contains no checks
     * to validate the arguments.
     *
     * @param spaceShip     players spaceship.
     * @param planets       planets to be available to the player.
     * @param currentPlanet planet to start at. Can be null.
     * @param currentDay    day that the player should start on.
     * @param duration      number of days the game should run.
     */
    public GameState(final SpaceShip spaceShip, final Set<Planet> planets, final Planet currentPlanet,
                     final int currentDay, final int duration) {
        this();
        /*
         If no current planet was given then its likely we are starting a
         new game so just pick one.
         */
        if (currentPlanet == null) {
            Optional<Planet> startPlanet = planets.stream().findAny();
            startPlanet.ifPresent(planet -> this.currentPlanet = planet);
        }

        // Cannot have nulls for these variables.
        this.spaceShip = Objects.requireNonNull(spaceShip);
        this.planets = Objects.requireNonNull(planets);
        this.currentDay = currentDay;
        this.duration = duration;
        this.score = 0;

        traders = new SpaceTraders();
    }

    /**
     * Initialises the event handlers for the class. Provided in this
     * form to support Gson when creating object instances.
     */
    private GameState() {
        GameEnvironment.EVENT_MANAGER.addObserver(Event.START_DAY, new NewDayHandler());
    }

    /**
     * Save the game to the file-system. The game state is saved to a JSON file.
     *
     * @param gameState state to be saved to file.
     * @param filename  URI to save the state to.
     * @throws IOException if unable to write to the file-system.
     */
    public static void saveState(
            final GameState gameState, final String filename) throws IOException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting().create();
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
    public static GameState loadState(final String filename)
            throws IOException {
        Path file = Paths.get(filename);
        String json = Files.readString(file);
        Gson gson = new Gson();
        return gson.fromJson(json, GameState.class);
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
     * Returns a set of planets that are available for this game state.
     *
     * @return set of planets.
     */
    public Set<Planet> getPlanets() {
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
    public void setCurrentPlanet(final Planet newPlanet) throws IllegalArgumentException {
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
    public Planet planetFromName(final String name) {
        for (Planet planet : planets) {
            if (planet.toString().equals(name)) {
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

    /**
     * Returns if the space ship is still missing parts.
     *
     * @return true if the space ship is still missing parts.
     */
    public boolean isMissingShipParts() {
        return spaceShip.getMissingParts() > 0;
    }

    /**
     * Calculates the current score of the game state.
     */
    public void computeScore() {
        score = 0;
        spaceShip.getShipCrew().forEach(crewMember -> score += 100);
        score += spaceShip.getShieldCount() * 500;
        spaceShip.getShipItems().forEach((key, value) -> score += 50);
        score += (spaceShip.getOriginalMissingParts() - spaceShip.getMissingParts()) * 1000;
        score += (duration - currentDay) * 1000;
        score += spaceShip.getBalance() * 10;
    }

    /**
     * Returns the current score of the game state.
     *
     * @return current GameState score.
     */
    public int getScore() {
        return score;
    }

    /**
     * GameState handler for the "NEW_DAY" event. Checks if the game state
     * can move to the next day, if so then checks if a random start of day
     * event should be triggered, increases the current day and recomputes
     * the score. If there are no more days then the "DEFEAT" event is
     * triggered.
     */
    final class NewDayHandler implements Observer {

        @Override
        public void onEvent(final Object... args) {
            if (!hasNextDay()) {
                GameEnvironment.EVENT_MANAGER.notifyObservers(
                        Event.DEFEAT, "On no! It seems you have failed to rebuild your ship in time! Err....");
            }
            if (SpaceExplorer.randomGenerator.nextBoolean()) {
                GameEnvironment.EVENT_MANAGER.notifyObservers(
                        Event.RANDOM_EVENT, EventTrigger.START_DAY, GameState.this);
            }

            currentDay += 1;
            computeScore();
        }
    }
}
