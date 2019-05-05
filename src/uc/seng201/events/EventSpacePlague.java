package uc.seng201.events;

import uc.seng201.SpaceShip;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.helpers.Helpers;

public class EventSpacePlague implements IRandomEvent {
    @Override
    public void onTrigger(SpaceShip spaceShip) {
        int crewToInfectIndex = Helpers.randomGenerator.nextInt(spaceShip.getShipCrew().size());
        spaceShip.getShipCrew().get(crewToInfectIndex).addModification(Modifications.SPACE_PLAGUE);
    }
}
