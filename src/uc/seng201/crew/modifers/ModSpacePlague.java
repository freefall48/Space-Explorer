package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Space plague modifier.
 */
public class ModSpacePlague extends ModificationAdapter {

    /**
     * The base value that the crew members health regen should be
     * reduced by.
     */
    private static final int ALTERED_HEALTH_REGEN = -10;
    /**
     * When added to a crew member, makes their health regen negative.
     *
     * @param crewMember to have the modification added.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {
        int alteredRegen = ALTERED_HEALTH_REGEN;

        // Make sure the crew member will be losing health.
        if (crewMember.getHealthRegen() > 0) {
            alteredRegen -= crewMember.getHealthRegen();
        }
        crewMember.alterHealthRegen(alteredRegen);
    }

    /**
     * When removed resets the crew members health regen back to their original
     * regen.
     *
     * @param crewMember who had the modification removed.
     */
    @Override
    public void onRemove(final CrewMember crewMember) {
        // Restore the crew members stats.
        crewMember.restoreHealthRegen();
    }
}
