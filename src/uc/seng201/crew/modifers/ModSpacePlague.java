package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModSpacePlague extends ModificationAdapter {
    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.setHealthRegen(0);
    }

    @Override
    public void onTick(CrewMember crewMember) {
        crewMember.alterHealth(-15);
    }

    @Override
    public void onRemove(CrewMember crewMember) {
        crewMember.setHealthRegen(crewMember.getBaseHealthRegen());
    }
}
