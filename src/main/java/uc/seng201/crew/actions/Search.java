package uc.seng201.crew.actions;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;

import java.io.IOException;

public class Search {

    public static void onPerform() throws IOException {
        onPerform(SpaceExplorer.getCurrentlyActingCrewMember());
    }

    public static void onPerform(CrewMember crewMember) throws IOException {
        crewMember.performAction();
        String message = SpaceExplorer.getCurrentPlanet().onSearch(crewMember);
        System.out.println(message);
        Helpers.waitForEnter();

    }
}
