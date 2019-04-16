package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.crew.Human;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleGame {

    public static void main(String[] args) {
        try {
            int gameDuration = gameDurationInput();
            SpaceShip spaceShip = new SpaceShip(shipNameInput(), SpaceExplorer.calcPartsToFind(gameDuration));
            spaceShip.add(generateCrew());
            SpaceExplorer.runGame(spaceShip, gameDuration);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }

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
                    outputAvailableCrewTypes();
                    System.out.print(String.format("Enter crew member no. %d's type: ", i));
                    crewMemberType = CrewType.valueOf(Helpers.bufferedReader.readLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                }
            } while (crewMemberType == null);
            switch (crewMemberType) {
                case HUMAN:
                    crew.add(new Human(crewMemberName));
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

    private static void outputAvailableCrewTypes() {
        System.out.println("Available crew types:");
        for (CrewType crewType : CrewType.values()) {
            System.out.print(String.format("[%s] ", crewType));
        }
        System.out.print("\n");
    }
}
