package uc.seng201.events;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.helpers.Helpers;

public class EventSpacePlague implements IRandomEvent {
    @Override
    public void onTrigger(SpaceShip spaceShip) {
        for (CrewMember crewMember : spaceShip.getShipCrew(true)) {
            if (Helpers.randomGenerator.nextBoolean()) {
                crewMember.addModification(Modifications.SPACE_PLAGUE);
            }
        }
    }
}
