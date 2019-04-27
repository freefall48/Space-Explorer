package uc.seng201.targets.events;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Illnesses;
import uc.seng201.helpers.Helpers;

public class SpacePlauge implements IEvent {

    @Override
    public void onTriggered() {
        SpaceExplorer.getCurrentlyActingCrewMember().addIllness(Illnesses.SPACE_PLAGUE);
        for (CrewMember crewMember : SpaceExplorer.getSpaceShip().getShipCrew()) {
            if (Helpers.randomGenerator.nextBoolean()) {
                crewMember.addIllness(Illnesses.SPACE_PLAGUE);
            }
        }
    }
}
