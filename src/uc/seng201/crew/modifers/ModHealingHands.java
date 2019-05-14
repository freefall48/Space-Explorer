package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModHealingHands extends ModificationAdapter {
    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.alterHealthRegen((int) (crewMember.getHealthRegen() * 1.25));
    }
}
