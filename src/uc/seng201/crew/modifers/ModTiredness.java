package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;

/**
 * Tired modifier for when the crew member.
 */
public class ModTiredness extends ModificationAdapter {

    /**
     * The crew members food decay rate should be increased by.
     */
    private static final int INCREASED_FOOD_DECAY = 20;
    /**
     * Increased the crew members food decay rate and notifies the user
     * that the crew member is now tired.
     *
     * @param crewMember that has the modification added.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {
        // Increase the crew members food decay rate by 20.
        crewMember.alterFoodDecayRate(INCREASED_FOOD_DECAY);
        Display.popup(crewMember.toString() + " is tired. They better sleep.");
    }

    /**
     * Restores the crew members foods decay rate back to its original rate.
     *
     * @param crewMember to remove the effects from.
     */
    @Override
    public void onRemove(final CrewMember crewMember) {
        crewMember.restoreFoodDecayRate();
    }

    /**
     * While crew members have this modifier, reduce the number of actions
     * they can perform each day.
     *
     * @param crewMember who has the modifier.
     */
    @Override
    public void onTick(final CrewMember crewMember) {
        crewMember.alterActionsLeft(-1);
    }
}
