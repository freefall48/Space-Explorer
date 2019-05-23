package uc.seng201.misc.events;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.environment.GameState;

import java.util.Iterator;

/**
 * Random Event that can be triggered on the start of a new day. When triggered a random number of
 * crew members (at least one) are chosen to be infected. If a crew member is already infected then
 * they are skipped.
 */
public class EventSpacePlague implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        // How many crew members are to be infected. Make sure 1 - ALL are possible.
        int toInfect = SpaceExplorer.randomGenerator.nextInt(gameState.getSpaceShip().getShipCrew().size());
        int hasInfected = 0;
        if (toInfect == 0) {
            toInfect = 1;
        }

        // Let us construct the output we wish to return.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Oh no! The Space Plague has infected crew members: ");

        /*
         Get the iterator and loop over until we have infected enough crew members. The set is
         not returned in any particular order so its random enough.
         */
        Iterator<CrewMember> iterator = gameState.getSpaceShip().getShipCrew().iterator();
        for (int i = 0; i < toInfect; i++) {
            CrewMember crewMember = iterator.next();

            // Check if the crew member already has it. No point adding it again.
            if (!crewMember.getModifications().contains(Modifications.SPACE_PLAGUE)) {
                crewMember.addModification(Modifications.SPACE_PLAGUE);
                hasInfected += 1;
                stringBuilder.append(crewMember.toString());
                // Check if we need a comma.
                if (i + 1 < toInfect) {
                    stringBuilder.append(", ");
                }
            }
        }
        // Make sure we have actually infected someone. Its possible everyone is already infected.
        if (hasInfected > 0) {
            return stringBuilder.toString();
        }
        return null;
    }
}
