package uc.seng201.crew.modifers;

import uc.seng201.crew.CrewMember;

public class ModMechanicsToolbox extends ModificationAdapter {
    @Override
    public void onAdd(CrewMember crewMember) {
        crewMember.alterRepairAmount(20);
    }
}
