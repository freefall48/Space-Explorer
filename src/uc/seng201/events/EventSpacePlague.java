package uc.seng201.events;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.utils.Helpers;

public class EventSpacePlague implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        int crewToInfectIndex = Helpers.randomGenerator.nextInt(gameState.getSpaceShip().getShipCrew().size());
        CrewMember crewMember = gameState.getSpaceShip().getShipCrew().get(crewToInfectIndex);
        crewMember.addModification(Modifications.SPACE_PLAGUE);
        return String.format("%s is not feeling well...They look to have the Space Plague",
                crewMember.getName());
    }
}
