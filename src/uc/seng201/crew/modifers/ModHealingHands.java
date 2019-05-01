package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModHealingHands extends ModificationAdapter {
    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.setHealthRegen((int) (crewMember.getBaseHealthRegen() * 1.25));
    }
}
