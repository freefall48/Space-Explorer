package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;

/**
 * Tired modifier for when the crew member.
 */
public class ModTiredness extends ModificationAdapter {

    /**
     * Increased the crew members food decay rate and notifies the user
     * that the crew member is now tired.
     *
     * @param crewMember that has the modification added.
     */
    @Override
    public void onAdd(CrewMember crewMember) {
        // Increase the crew members food decay rate by 20.
        crewMember.alterFoodDecayRate(20);
        Display.popup(crewMember.toString() + " is tired. They better sleep.");
    }

    /**
     * Restores the crew members foods decay rate back to its original rate.
     *
     * @param crewMember to remove the effects from.
     */
    @Override
    public void onRemove(CrewMember crewMember) {
        crewMember.restoreFoodDecayRate();
    }

    /**
     * While crew members have this modifier, reduce the number of actions they can perform
     * each day.
     *
     * @param crewMember who has the modifier.
     */
    @Override
    public void onTick(CrewMember crewMember) {
        crewMember.alterActionsLeft(-1);
    }
}
