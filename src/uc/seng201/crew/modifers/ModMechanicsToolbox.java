package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Can only be added to a crew member. When they are given the Mechanics toolbox
 * they cant lose it.
 */
public class ModMechanicsToolbox extends ModificationAdapter {

    /**
     * The value to increase the crew members repair ability by.
     */
    private static final int INCREASED_REPAIR_ABILITY = 20;
    /**
     * Increases the crew members repair ability.
     *
     * @param crewMember to have the modification added.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {
        crewMember.alterRepairAmount(INCREASED_REPAIR_ABILITY);
    }
}
