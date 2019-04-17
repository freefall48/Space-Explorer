package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.Action;
import uc.seng201.crew.actions.Pilot;
import uc.seng201.crew.actions.Sleep;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceExplorer {

    private static SpaceShip spaceShip;
    private static CrewMember currentlyActingCrewMember;
    private static List<Planet> knownPlanets;
    private static Planet currentPlanet;

    public static SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public static CrewMember getCurrentlyActingCrewMember() {
        return currentlyActingCrewMember;
    }

    public static List<Planet> getKnownPlanets() {
        return knownPlanets;
    }

    public static Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public static void setCurrentPlanet(Planet currentPlanet) {
        SpaceExplorer.currentPlanet = currentPlanet;
    }

    static void runGame(SpaceShip ship, int duration) throws IOException {
        spaceShip = ship;
        generatePlanets(duration);
        currentPlanet = knownPlanets.get(0);
        for (int currentDay = 1; currentDay <= duration; currentDay++) {
            spaceShip.getShipCrew().forEach(CrewMember::updateStats);
            System.out.println(String.format("Day %d statistics (Orbiting %s):\n%s\n",
                    currentDay, currentPlanet.getPlanetName(), spaceShip.toString()));
            performCrewAction();
        }
    }

    private static void performCrewAction() throws IOException {
        List<CrewMember> availableCrew;
        do {
            availableCrew = availableCrewMembers();
            currentlyActingCrewMember = inputCrewMemberToAct(availableCrew);
            currentlyActingCrewMember.performAction();
            switch (actionToPerform(availableCrew.size())) {
                case SLEEP:
                    Sleep.onPerform();
                    break;
                case PILOT:
                    Pilot.onPerform();
                    break;
            }
        } while (!availableCrew.isEmpty());

    }

    private static Action actionToPerform(int availableCrew) throws IOException {
        List<Action> actions = Arrays.asList(Action.values());
        if (availableCrew < 2) {
            actions.remove(Action.PILOT);
        }
        System.out.println(String.format("Actions available: %s\n", Helpers.listToString(actions)));
        System.out.print(String.format("Enter action for %s: ", currentlyActingCrewMember.getName()));
        return Action.valueOf(Helpers.bufferedReader.readLine().toUpperCase());
    }


    public static CrewMember inputCrewMemberToAct(List<CrewMember> availableCrew) throws IOException {
        return inputCrewMemberToAct(availableCrew, false);
    }

    public static CrewMember inputCrewMemberToAct(List<CrewMember> availableCrew, boolean supplementary) throws IOException {
        CrewMember crewMember;
        do {
            if (supplementary) {
                System.out.println("This action requires an additional crew member...");
            } else {
                System.out.println("Choose a crew member to perform an action...");
            }
            System.out.println(String.format("Available crew members to choose:\n%s\n",
                    Helpers.listToString(availableCrew, true)));
            System.out.print("Enter the name of the crew member to act: ");
            String name = Helpers.bufferedReader.readLine();
            crewMember = spaceShip.findCrewMember(name);
        } while (crewMember == null);
        return crewMember;
    }

    public static List<CrewMember> availableCrewMembers() {
        List<CrewMember> availableCrew = new ArrayList<>();
        spaceShip.getShipCrew().forEach(crewMember -> {
            if (crewMember.canPerformActions()) {
                availableCrew.add(crewMember);
            }
        });
        return availableCrew;
    }

    static int calcPartsToFind(int duration) {
        return (int) (duration * 0.6666);
    }

    public static Planet findPlanet(String name) {
        for (Planet planet : knownPlanets) {
            if (planet.getPlanetName().equals(name)) {
                return planet;
            }
        }
        return null;
    }

    private static void generatePlanets(int numberOfPlanets) {
        knownPlanets = new ArrayList<>();
        for (int i = 0; i < numberOfPlanets; i++) {
            knownPlanets.add(new Planet());
        }
    }
}
