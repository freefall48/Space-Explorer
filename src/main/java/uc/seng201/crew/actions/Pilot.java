package uc.seng201.crew.actions;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;
import uc.seng201.targets.Planet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pilot {

    public static void onPerform() throws UnableToPerformAction, IOException {
        onPerform(SpaceExplorer.getCurrentlyActingCrewMember());
    }

    private static void onPerform(CrewMember pilot) throws UnableToPerformAction, IOException {
        CrewMember copilot = findCopilot(pilot);
        System.out.println(String.format("%s and %s JUMPED the ship!", pilot, copilot));
        Planet destinationPlanet = selectPlanet(pilot, copilot);
        SpaceExplorer.setCurrentPlanet(destinationPlanet);
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
            List<Planet> availablePlanets = new ArrayList<>(SpaceExplorer.getKnownPlanets());
            availablePlanets.remove(SpaceExplorer.getCurrentPlanet());
            System.out.println(String.format("%s and %s need a destination!\nAvailable planets are: %s\n",
                    pilot, copilot, Helpers.listToString(availablePlanets)));
            System.out.print("Enter the destination planet: ");
            String destinationName = Helpers.bufferedReader.readLine();
            destination = SpaceExplorer.findPlanet(destinationName);
        } while (destination == null);
        return destination;
    }
}
