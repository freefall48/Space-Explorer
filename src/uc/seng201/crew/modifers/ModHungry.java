package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;

/**
 * When the crew member has a food level of 0 they should have adverse effects.
 */
public class ModHungry extends ModificationAdapter {

    /**
     * Crew members health regen should be reduced by.
     */
    private static final int REDUCED_HEALTH_REGEN = -10;
    /**
     * The value the crew members tiredness rate should be
     * increased by.
     */
    private static final int INCREASED_TIREDNESS_RATE = 20;
    /**
     * Reduces the crew members health regen as well as increases the rate
     * at which they become tired.
     *
     * @param crewMember to add effects to.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {

        /*
         Some crew members may starve some might have high enough regen.
         But they will recover very slowly. Increase the crew members
         tiredness rate too.
         */

        crewMember.alterHealthRegen(REDUCED_HEALTH_REGEN);
        crewMember.alterTirednessRate(INCREASED_TIREDNESS_RATE);

        // Inform the user.
        Display.popup(crewMember.toString() + " is hungry. Better feed them soon.");
    }

    /**
     * Restores the crew member to their normal stats.
     *
     * @param crewMember to remove effects from.
     */
    @Override
    public void onRemove(final CrewMember crewMember) {
        // Restore the crew member to their normal stats.
        crewMember.restoreHealthRegen();
        crewMember.restoreTirednessRate();
    }
}
