package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.errors.ActionException;
import uc.seng201.items.SpaceItem;

public class ActionConsumeItem implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {

        if (args.length != 1 && crewMembers.length != 1) {
            throw new ActionException("Invalid args provided");
        }
        if (args[0] instanceof SpaceItem) {
            SpaceItem item = (SpaceItem) args[0];
            gameState.getSpaceShip().remove(item);
            (item).onConsume(crewMembers[0]);
        }
        return null;
    }
}
