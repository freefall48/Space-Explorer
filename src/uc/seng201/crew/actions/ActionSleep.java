package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.errors.ActionException;

public class ActionSleep implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        if (crewMembers.length != 1) {
            throw new ActionException("Invalid args given");
        }
        crewMembers[0].alterTiredness(-crewMembers[0].getMaxTiredness());
        return null;
    }
}
