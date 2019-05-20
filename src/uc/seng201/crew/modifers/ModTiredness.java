package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;

public class ModTiredness extends ModificationAdapter {

    @Override
    public void onAdd(CrewMember crewMember) {
        // Increase the crew members food decay rate by 20.
        crewMember.alterFoodDecayRate(20);
        Display.popup(crewMember.toString() + " is tired. They better sleep.");
    }

    @Override
    public void onRemove(CrewMember crewMember) {
        crewMember.restoreFoodDecayRate();
    }

    @Override
    public void onTick(CrewMember crewMember) {
        crewMember.alterActionsLeft(-1);
    }
}
