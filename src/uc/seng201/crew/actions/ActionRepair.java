package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.errors.ActionException;

/**
 * Action for a crew member repairing the spaceship.
 */
public final class ActionRepair implements IAction {
    @Override
    public String perform(final GameState gameState, final Object[] args,
                          final CrewMember... crewMembers) {
        if (crewMembers.length != 1) {
            throw new ActionException("Invalid args provided");
        }
        gameState.getSpaceShip().repair(crewMembers[0].getRepairAmount());
        return null;
    }
}
