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

import javax.swing.*;
import java.util.List;

public class GameState {

    // WARNING: THESE VARIABLES ARE MUTABLE
    private SpaceShip spaceShip;
    private List<Planet> planets;
    private SpaceTraders traders;

    private Planet currentPlanet;
    private int currentDay;
    private int duration;
    private int score;

    public GameState(SpaceShip spaceShip, int duration, List<Planet> planets) {
        this(spaceShip, planets, planets.get(0), 1, duration);
    }

    public GameState(SpaceShip spaceShip, List<Planet> planets, Planet currentPlanet, int currentDay, int duration) {
        this.spaceShip = spaceShip;
        this.planets = planets;
        this.currentPlanet = currentPlanet;
        this.currentDay = currentDay;
        this.duration = duration;
        this.score = 0;

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
        for (CrewMember crewMember: spaceShip.getShipCrew()) {
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
        score += spaceShip.getSpaceBucks() * 10;
    }

    public int getScore() {
        return score;
    }

}