package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.Action;
import uc.seng201.crew.actions.Pilot;
import uc.seng201.crew.actions.Sleep;
import uc.seng201.helpers.Helpers;
import uc.seng201.helpers.StateActions;
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
    private static int gameDuration;
    private static int currentDay;

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

    static void LoadedGame(SpaceShip ship, List<Planet> planets, CrewMember currentAct,
                           Planet orbiting, int day, int duration) throws IOException {
        spaceShip = ship;
        knownPlanets = planets;
        currentlyActingCrewMember = currentAct;
        currentPlanet = orbiting;
        runGame(day, duration);
    }

    static void NewGame(SpaceShip ship, int duration) throws IOException {
        spaceShip = ship;
        generatePlanets(duration);
        currentPlanet = knownPlanets.get(0);
        runGame(1, duration);
    }

    private static void runGame(int day, int duration) throws IOException {
        gameDuration = duration;
        currentDay = day;
        for (; currentDay <= duration; currentDay++) {
            spaceShip.getShipCrew().forEach(CrewMember::updateStats);
            inputMenuAction();
            printDayStats();
            performCrewAction();
        }
    }

    private static void printDayStats() {
        System.out.println(String.format("\n\n#######\tDay %d statistics (Orbiting %s)\t#######\n%s\n",
                currentDay, currentPlanet.getPlanetName(), spaceShip.toString()));
    }

    private static void inputMenuAction() throws IOException {
        System.out.println(String.format("\n\n#######\tStart of day %d!\t#######", currentDay));
        System.out.print("Would you like to [B]egin the day or [S]ave the game? : ");
        String choice = Helpers.bufferedReader.readLine().toUpperCase();
        switch (choice) {
            case "B":
                return;
            case "S":
                System.out.print("Filename to save as: ");
                String filename = Helpers.bufferedReader.readLine();
                boolean saved = StateActions.saveState(filename, spaceShip, knownPlanets, currentlyActingCrewMember,
                        currentPlanet, currentDay, gameDuration);
                System.out.println(saved);
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
