package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModHungry extends ModificationAdapter {

    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.setHealthRegen(-10);
        crewMember.alterTirednessRate(20);
    }

    @Override
    public void onRemove(CrewMember crewMember) {
        crewMember.restoreHealthRegen();
        crewMember.restoreTirednessRate();
    }
}
