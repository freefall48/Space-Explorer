package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModFriendly extends ModificationAdapter {
    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.setRepairAmount(2);
    }
}
