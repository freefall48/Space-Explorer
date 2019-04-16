package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.Action;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpaceExplorer {

    private static int gameDuration;
    private static SpaceShip spaceShip;
    private static CrewMember currentlyActingCrewMember;
    private static List<Planet> knownPlanets = new ArrayList<>();

    public static SpaceShip getSpaceShip() {
        return spaceShip;
    }
    public static CrewMember getCurrentlyActing() {
        return currentlyActingCrewMember;
    }

    static void runGame(SpaceShip ship, int duration) throws IOException {
        gameDuration = duration;
        spaceShip = ship;
        while (gameDuration > 0) {
            spaceShip.getShipCrew().forEach(CrewMember::updateStats);
            performCrewAction();
            gameDuration -= 1;
        }
    }

    private static void performCrewAction() throws IOException {
        boolean actionsLeft = setCrewMemberToAct();
        while (actionsLeft) {
            Action action = actionToPerform();
            if (!currentlyActingCrewMember.performAction()) {
                throw new IllegalStateException();
            }
            switch (action) {
                case SLEEP:
                    System.out.println("Sleep");
                    break;
                case PILOT:
                    System.out.println("Pilot");
                    break;
            }
            actionsLeft = setCrewMemberToAct();
        }
    }

    private static Action actionToPerform() throws IOException {
        System.out.println(String.format("Actions available: %s\n", Helpers.listToString(Action.values())));
        System.out.print(String.format("Enter action for %s: ", currentlyActingCrewMember.getName()));
        return Action.valueOf(Helpers.bufferedReader.readLine().toUpperCase());
    }

    private static boolean setCrewMemberToAct() throws IOException {
        CrewMember crewMember;
        do {
            List<CrewMember> availableCrew = availableCrewMembers();
            if (availableCrew.isEmpty()) {
                return false;
            }
            System.out.println(String.format("Available crew members to choose:\n%s\n",
                    Helpers.listToString(availableCrew, true)));
            System.out.print("Enter the name of the crew member to act: ");
            String name = Helpers.bufferedReader.readLine();
            crewMember = spaceShip.find(name);
        } while (crewMember == null);
        currentlyActingCrewMember = crewMember;
        return true;

    }

    private static List<CrewMember> availableCrewMembers() {
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
}
