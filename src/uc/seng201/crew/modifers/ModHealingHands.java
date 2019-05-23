package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Increases a crew members health while this modification is active.
 */
public class ModHealingHands extends ModificationAdapter {

    /**
     * Health regen multiplier to be applied to crew members who
     * have this modifier.
     */
    private static final double HEALTH_REGEN_MULTIPLIER = 1.25;

    /**
     * Increase the current healing ability oif this crew member.
     *
     * @param crewMember to add effect to.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {
        crewMember.alterHealthRegen(
                (int) (crewMember.getHealthRegen() * HEALTH_REGEN_MULTIPLIER));
    }

    /**
     * Resets the crew members health regen back to its original value.
     *
     * @param crewMember to remove effect from.
     */
    @Override
    public void onRemove(final CrewMember crewMember) {
        crewMember.restoreHealthRegen();
    }
}
