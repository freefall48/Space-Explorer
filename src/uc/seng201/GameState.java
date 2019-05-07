package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.destinations.traders.SpaceTraders;
import uc.seng201.errors.InvalidGameState;
import uc.seng201.destinations.Planet;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.IRandomEvent;
import uc.seng201.events.RandomEvent;
import uc.seng201.helpers.Helpers;

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
        this.spaceShip = spaceShip;
        this.planets = planets;
        this.currentPlanet = currentPlanet;
        this.currentDay = currentDay;
        this.duration = duration;
        this.score = 0;

        traders = new SpaceTraders();

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

    private void nextDayCrew() {
        spaceShip.getShipCrew().forEach(crewMember -> {

            crewMember.alterFood(crewMember.getFoodDecayRate());
            if (crewMember.getFoodLevel() == 0) {
                crewMember.setHealthRegen(-20);
            }

            crewMember.alterHealth(crewMember.getCurrentHealthRegen());

            crewMember.alterTiredness(crewMember.getTirednessRate());
            if (crewMember.getTiredness() == crewMember.getMaxTiredness()) {
                crewMember.setActionsLeftToday(1);
            } else {
                crewMember.setActionsLeftToday(2);
            }

            for (Modifications modification : crewMember.getModifications()) {
                modification.getInstance().onTick(crewMember);
            }

            if (!crewMember.isAlive()) {
                spaceShip.remove(crewMember);
            }
        });
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

    private String nextDayRandomEvent() {
        if (Helpers.randomGenerator.nextBoolean()) {
            RandomEvent event = IRandomEvent.eventToTrigger(EventTrigger.START_DAY);
            event.getInstance().onTrigger(spaceShip);
            return event.getEventDescription();
        }
        return null;
    }

    public String nextDay() throws InvalidGameState {
        if (!hasNextDay()) {
            SpaceExplorer.endGame("On no! It seems you have failed to rebuild your ship in time! Err....",
                    false);
        }
        if (spaceShip.getShipCrew().size() == 0) {
            SpaceExplorer.endGame("Looks like you have run out of crew...", false);
        }
        if (spaceShip.getShieldCount() == 0) {
            SpaceExplorer.endGame("Looks like you have managed to destroy whats left of " + spaceShip.getShipName(),
                    false);
        }

        currentDay += 1;
        nextDayCrew();
        nextDayTraders();
        String eventMessage = nextDayRandomEvent();
        computeScore();

        return eventMessage;

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
