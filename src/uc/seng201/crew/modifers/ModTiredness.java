package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModTiredness extends ModificationAdapter {

    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.alterFoodDecayRate(20);
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
