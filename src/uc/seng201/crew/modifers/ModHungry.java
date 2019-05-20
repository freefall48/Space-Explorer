package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;

public class ModHungry extends ModificationAdapter {

    @Override
    public void onAdd(CrewMember crewMember) {

        /*
         Some crew members may starve some might have high enough regen. But they will recover very slowly.
         Increase the crew members tiredness rate too.
         */
        crewMember.alterHealthRegen(-10);
        crewMember.alterTirednessRate(20);

        // Inform the user.
        Display.popup(crewMember.toString() + " is hungry. Better feed them soon.");
    }

    @Override
    public void onRemove(CrewMember crewMember) {
        // Restore the crew member to their normal stats.
        crewMember.restoreHealthRegen();
        crewMember.restoreTirednessRate();
    }
}
