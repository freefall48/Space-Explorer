package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.errors.ActionException;

public class ActionRepair implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        if (crewMembers.length != 1) {
            throw new ActionException("Invalid args provided");
        }
        gameState.getSpaceShip().alterShield(crewMembers[0].getRepairAmount());
        return null;
    }
}
