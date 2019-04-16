package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;

public class Pilot {

    public static void onPerform(CrewMember pilot, CrewMember copilot, String target)throws UnableToPerformAction {
        if (pilot.performAction() && copilot.performAction()) {
            // GO to new planet
        } else {
            throw new UnableToPerformAction();
        }
    }
}
