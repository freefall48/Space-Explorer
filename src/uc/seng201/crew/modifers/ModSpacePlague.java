package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModSpacePlague extends ModificationAdapter {

    @Override
    public void onAdd(CrewMember crewMember) {
        int alteredRegenAmount = -10;

        // Make sure the crew member will be losing health.
        if (crewMember.getHealthRegen() > 0) {
            alteredRegenAmount -= crewMember.getHealthRegen();
        }
        crewMember.alterHealthRegen(alteredRegenAmount);
    }

    @Override
    public void onRemove(CrewMember crewMember) {
        // Restore the crew members stats.
        crewMember.restoreHealthRegen();
    }
}
