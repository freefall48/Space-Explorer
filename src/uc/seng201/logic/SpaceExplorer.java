package uc.seng201.logic;

import uc.seng201.logic.crew.CrewMember;
import uc.seng201.logic.crew.Sarah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpaceExplorer {

    private static int gameDuration;
    private static int partsToFind;
    private static String shipName;

    public static int getGameDuration() {
        return gameDuration;
    }

    public static int getPartsToFind() {
        return partsToFind;
    }

    public static String getShipName() {
        return shipName;
    }

    public static void main(String[] args) {
        setupGame();
        CrewMember crewMember = new Sarah("Sarah");
        SpaceShip ship = new SpaceShip("Hello", new CrewMember[]{});
        System.out.println(ship + "\n" + gameDuration + "\n" + partsToFind + "\n" + shipName);
    }

    public static void setupGame() {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter your ship name: ");
            shipName = bufferedReader.readLine();
        } catch (IOException e){
            System.out.println(e.toString());
            System.exit(1);
        }
        boolean continueLoop = true;
        do {
            System.out.print("Enter game duration (3 - 10 days): ");
            try {
                int duration = Integer.parseInt(bufferedReader.readLine());
                if (duration <= 10 && duration >= 3){
                    gameDuration = duration;
                    partsToFind = (int) (duration * 0.666);
                    continueLoop = false;
                }
            } catch (IOException e) {

            }
        } while (continueLoop);
    }
}
