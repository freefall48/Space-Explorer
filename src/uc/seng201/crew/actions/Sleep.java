package uc.seng201.crew.actions;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;

public class Sleep {

    public static void onPerform() throws UnableToPerformAction {
        onPerform(SpaceExplorer.getCurrentlyActingCrewMember());
    }

    private static void onPerform(CrewMember crewMember) throws UnableToPerformAction {
            crewMember.alterTiredness(-100);
    }
}
