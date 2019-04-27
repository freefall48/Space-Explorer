package uc.seng201.crew.actions;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pilot {

    public static void onPerform() throws IOException {
        onPerform(SpaceExplorer.getCurrentlyActingCrewMember());
    }

    private static void onPerform(CrewMember pilot) throws IOException {
        pilot.performAction();
        CrewMember copilot = findCopilot(pilot);
        Planet destinationPlanet = selectPlanet(pilot, copilot);
        SpaceExplorer.setCurrentPlanet(destinationPlanet);
        System.out.println(String.format("%s and %s JUMPED the ship to %s!",
                pilot.getName(), copilot.getName(), destinationPlanet.getPlanetName()));
        Helpers.waitForEnter();
    }

    private static CrewMember findCopilot(CrewMember pilot) throws IOException {
        while (true) {
            List<CrewMember> availableCrew = new ArrayList<>(SpaceExplorer.availableCrewMembers());
            availableCrew.remove(pilot);
            return SpaceExplorer.inputCrewMemberToAct(availableCrew, true);
        }

    }

    private static Planet selectPlanet(CrewMember pilot, CrewMember copilot) throws IOException {
        Planet destination;
        do {
            Planet currentPlanet = SpaceExplorer.getCurrentPlanet();
            List<Planet> availablePlanets = new ArrayList<>(SpaceExplorer.getKnownPlanets());
            availablePlanets.remove(currentPlanet);
            System.out.println(String.format("%s and %s need a destination!\nAvailable planets are: %s\n",
                    pilot.getName(), copilot.getName(), Helpers.listToString(availablePlanets)));
            System.out.print("Enter the destination planet: ");
            String destinationName = Helpers.bufferedReader.readLine().toUpperCase();
            destination = SpaceExplorer.findPlanet(destinationName);
        } while (destination == null);
        return destination;
    }
}
