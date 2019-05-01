package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public interface IModification {
    void onAdd(CrewMember crewMember);

    void onTick(CrewMember crewMember);

    void onRemove(CrewMember crewMember);
}
