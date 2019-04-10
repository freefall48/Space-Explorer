package uc.seng201.logic;

import uc.seng201.logic.crew.CrewMember;
import uc.seng201.logic.crew.CrewType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpaceExplorer {

    private static int gameDuration;
    private static SpaceShip spaceShip;

    /**
     * Used to output to the console. IDEs dont use a standard console
     * so this allows user input to be gathered reliably.
     */
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static int getGameDuration() {
        return gameDuration;
    }


    public static void main(String[] args) {
        setupGame();
//        System.out.println(ship + "\n" + gameDuration + "\n" + partsToFind + "\n" + ship.getShipName());
    }

    public static void setupGame() {
        try {
            gameDurationInput();
            spaceShip = new SpaceShip(shipNameInput(), calcPartsToFind());
            generateCrew();
        } catch (IOException e){
            System.out.println(e.toString());
            System.exit(1);
        }

    }

    public static void outputCrewTypes() {
        System.out.println("Available crew types:");
        for (CrewType crewType : CrewType.values())
        {
            System.out.print(String.format("[%s] ", crewType));
        }
        System.out.print("\n");
    }

    public static void generateCrew() throws IOException {
        int crewCount = crewCountInput();
        CrewType crewMemberType = null;
        String crewMemberName = null;
        for (int i = 1; i <= crewCount; i++) {
            do {
                System.out.print(String.format("Enter crew member no. %d's name: ", i));
                crewMemberName = bufferedReader.readLine();
            } while (!crewMemberName.matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$"));
            do {
                try {
                    outputCrewTypes();
                    System.out.print(String.format("Enter crew member no. %d's type: ", i));
                    crewMemberType = CrewType.valueOf(bufferedReader.readLine().toUpperCase());
                } catch (IllegalArgumentException e) {}
            } while (crewMemberType == null);
        }
        spaceShip.addCrewMember(new CrewMember(crewMemberName, crewMemberType));
    }

    public static int crewCountInput() throws IOException {
        int crewCount = 0;
        do {
            System.out.print("Enter desired crew population: (2 - 4): ");
            String userInput = bufferedReader.readLine();
            if (Helpers.intTryParse(userInput)) {
                crewCount = Integer.parseInt(userInput);
            }
        } while (crewCount < 2 || crewCount > 4);
        return crewCount;
    }

    public static String shipNameInput() throws IOException {
        String shipName;
        do {
            System.out.print("Enter the name of your ship: ");
            shipName = bufferedReader.readLine();
        } while (!shipName.matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$"));
        return shipName;
    }

    public static void gameDurationInput() throws IOException {
        int duration = 0;
        do {
            System.out.print("Enter game duration (3 - 10 days): ");
            String userInput = bufferedReader.readLine();
            if (Helpers.intTryParse(userInput)) {
                duration = Integer.parseInt(userInput);
            }
        } while (duration < 3 || duration > 10);
        gameDuration = duration;
    }
    public static int calcPartsToFind() {
        return (int) (getGameDuration() * 0.6666);
    }
}
