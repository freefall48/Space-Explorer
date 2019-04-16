package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;

public class Sleep {

    public static void onPerform(CrewMember crewMember) throws UnableToPerformAction {
        if (crewMember.performAction()) {
            crewMember.alterTiredness(-100);
        } else {
            throw new UnableToPerformAction();
        }

    }
}
