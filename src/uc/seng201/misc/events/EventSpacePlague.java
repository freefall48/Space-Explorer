package uc.seng201.misc.events;

import uc.seng201.environment.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;

import java.util.Iterator;

public class EventSpacePlague implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        int crewToInfectIndex = SpaceExplorer.randomGenerator.nextInt(gameState.getSpaceShip().getShipCrew().size());
        Iterator<CrewMember> iterator = gameState.getSpaceShip().getShipCrew().iterator();
        for (int i = 0; i < crewToInfectIndex; i++) {
            iterator.next();
        }
        CrewMember crewMember = iterator.next();
        crewMember.addModification(Modifications.SPACE_PLAGUE);
        return String.format("%s is not feeling well...They look to have the Space Plague",
                crewMember.getName());
    }
}
