package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.destinations.Planet;
import uc.seng201.errors.ActionException;

public class ActionSearch implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        if (args.length != 1 && crewMembers.length != 1) {
            throw new ActionException("Invalid args passed");
        }
        if (args[0] instanceof Planet) {
            return ((Planet) args[0]).onSearch(crewMembers[0], gameState.getSpaceShip());
        }
        throw new ActionException("Was not given a planet");
    }
}
