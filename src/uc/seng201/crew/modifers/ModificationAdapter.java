package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Crew member modification adapter. Overrides the methods of the interface
 * so modifications can implement only the methods required.
 */
public class ModificationAdapter implements IModification {

    /**
     * Triggered when the crew member has the modification added to them.
     *
     * @param crewMember to have the modification added.
     */
    @Override
    public void onAdd(final CrewMember crewMember) {
    }

    /**
     * Triggered when the crew member has the modification and a new
     * day is started.
     *
     * @param crewMember who has the modification.
     */
    @Override
    public void onTick(final CrewMember crewMember) {
    }

    /**
     * Triggered when the crew member has the modification removed.
     *
     * @param crewMember who had the modification removed.
     */
    @Override
    public void onRemove(final CrewMember crewMember) {
    }
}
