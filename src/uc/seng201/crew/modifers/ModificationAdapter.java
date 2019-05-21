package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

/**
 * Crew member modification adapter. Overrides the methods of the interface
 * so modifications can implement only the methods required.
 */
public class ModificationAdapter implements IModification {

    @Override
    public void onAdd(CrewMember crewMember){
    }

    @Override
    public void onTick(CrewMember crewMember){}

    @Override
    public void onRemove(CrewMember crewMember){}
}
