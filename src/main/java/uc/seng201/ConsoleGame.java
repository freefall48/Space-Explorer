package uc.seng201;

import uc.seng201.crew.*;
import uc.seng201.errors.InvalidGameState;
import uc.seng201.helpers.Helpers;
import uc.seng201.helpers.StateActions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleGame {

    public static void main(String[] args) {
        try {
            inputConsoleMenu();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }

    }

    private static void newGame() throws IOException {
        int gameDuration = gameDurationInput();
        SpaceShip spaceShip = new SpaceShip(shipNameInput(), SpaceExplorer.calcPartsToFind(gameDuration));
        spaceShip.add(generateCrew());
        SpaceExplorer.NewGame(spaceShip, gameDuration);
    }

    private static void loadGame() throws IOException {
        System.out.print("Filename: ");
        String filename = Helpers.bufferedReader.readLine();
        GameState gameState = StateActions.loadState(filename);
        if (gameState != null) {
            SpaceExplorer.LoadedGame(gameState.getSpaceShip(), gameState.getPlanets(), gameState.getCurrentActingMember(),
                    gameState.getCurrentPlanet(), gameState.getCurrentDay(), gameState.getDuration());
        } else {
            throw new InvalidGameState();
        }
    }

    private static void inputConsoleMenu() throws IOException {
        do {
            System.out.println("#######\tMain Menu\t#######");
            System.out.print("Would you like to start a ([N]ew game) or ([L]oad) an existing game? : ");
            String action = Helpers.bufferedReader.readLine();
            switch (action.toUpperCase()) {
                case "N":
                    newGame();
                    break;
                case "L":
                    loadGame();
                    break;
            }
        } while (true);
    }

    private static List<CrewMember> generateCrew() throws IOException {
        List<CrewMember> crew = new ArrayList<>();
        int crewCount = crewCountInput();
        CrewType crewMemberType = null;
        String crewMemberName;
        for (int i = 1; i <= crewCount; i++) {
            do {
                System.out.print(String.format("Enter crew member no. %d's name: ", i));
                crewMemberName = Helpers.bufferedReader.readLine();
            } while (!crewMemberName.matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$"));
            do {
                try {
                    System.out.println(String.format("Available crew types: %s", Helpers.listToString(CrewType.values())));
                    System.out.print(String.format("Enter crew member no. %d's type: ", i));
                    crewMemberType = CrewType.valueOf(Helpers.bufferedReader.readLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                }
            } while (crewMemberType == null);
            switch (crewMemberType) {
                case HUMAN:
                    crew.add(new Human(crewMemberName));
                    break;
                case CRYSTAL:
                    crew.add(new Crystal(crewMemberName));
                    break;
                case ENGI:
                    crew.add(new Engi((crewMemberName)));
                    break;
                case SLUG:
                    crew.add(new Slug(crewMemberName));
                    break;
                case LANIUS:
                    crew.add(new Lanius(crewMemberName));
                    break;
                case ZOLTAN:
                    crew.add(new Zoltan(crewMemberName));
                    break;
            }
        }
        return crew;
    }

    private static int crewCountInput() throws IOException {
        int crewCount = 0;
        do {
            System.out.print("Enter desired crew population: (2 - 4): ");
            String userInput = Helpers.bufferedReader.readLine();
            if (Helpers.intTryParse(userInput)) {
                crewCount = Integer.parseInt(userInput);
            }
        } while (crewCount < 2 || crewCount > 4);
        return crewCount;
    }

    private static String shipNameInput() throws IOException {
        String shipName;
        do {
            System.out.print("Enter the name of your ship: ");
            shipName = Helpers.bufferedReader.readLine();
        } while (!shipName.matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$"));
        return shipName;
    }

    private static int gameDurationInput() throws IOException {
        int duration = 0;
        do {
            System.out.print("Enter game duration (3 - 10 days): ");
            String userInput = Helpers.bufferedReader.readLine();
            if (Helpers.intTryParse(userInput)) {
                duration = Integer.parseInt(userInput);
            }
        } while (duration < 3 || duration > 10);
        return duration;
    }
}
