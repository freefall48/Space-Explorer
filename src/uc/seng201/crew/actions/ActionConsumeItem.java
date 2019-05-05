package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.errors.ActionException;
import uc.seng201.items.Items;

public class ActionConsumeItem implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {

        if (args.length != 1 && crewMembers.length != 1) {
            throw new ActionException("Invalid args provided");
        }
        if (args[0] instanceof Items) {
            gameState.getSpaceShip().remove((Items) args[0]);
            ((Items) args[0]).onConsume(crewMembers[0]);
        }
        return null;
    }
}
