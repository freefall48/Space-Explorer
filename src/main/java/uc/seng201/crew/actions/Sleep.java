package uc.seng201.crew.actions;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;

public class Sleep {

    public static void onPerform() {
        onPerform(SpaceExplorer.getCurrentlyActingCrewMember());
    }

    private static void onPerform(CrewMember crewMember) {
        crewMember.performAction();
        crewMember.alterTiredness(-100);
    }
}
