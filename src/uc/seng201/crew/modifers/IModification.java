package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Crew member modification interface.
 */
public interface IModification {
    /**
     * When the modification is added to the crew member this method is called.
     *
     * @param crewMember to had the modification added.
     */
    void onAdd(CrewMember crewMember);

    /**
     * While the crew member has this modification this method is performed
     * at the start of each day.
     *
     * @param crewMember who has the modification.
     */
    void onTick(CrewMember crewMember);

    /**
     * When the modification is removed this method is performed.
     *
     * @param crewMember who had the modification removed.
     */
    void onRemove(CrewMember crewMember);
}
