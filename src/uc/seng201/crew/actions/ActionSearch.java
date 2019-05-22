package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;
import uc.seng201.errors.ActionException;

/**
 * Action for crew members who are searching the planet that is
 * currently orbited.
 */
public final class ActionSearch implements IAction {
    @Override
    public String perform(final GameState gameState, final Object[] args,
                          final CrewMember... crewMembers) {
        if (args.length != 1 || crewMembers.length != 1) {
            throw new ActionException("Invalid args passed");
        }
        if (args[0] instanceof Planet) {
            return ((Planet) args[0]).onSearch(crewMembers[0],
                    gameState.getSpaceShip());
        }
        throw new ActionException("Was not given a planet");
    }
}
