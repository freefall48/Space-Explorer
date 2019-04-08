package uc.seng201.logic.medical;

import uc.seng201.logic.crew.CrewMember;

public class Stardew extends MedicalItem {

    public void onUse(CrewMember crewMember) {
        crewMember.removeIllness(Illnesses.SPACE_PLAGUE);
    }
}
