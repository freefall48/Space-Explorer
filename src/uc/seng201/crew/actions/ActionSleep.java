package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.errors.ActionException;

import java.awt.*;

/**
 * Action for crew members to sleep and reduce their current
 * tiredness level.
 */
public final class ActionSleep extends ConsumeActionPoint {
    @Override
    public String perform(final GameState gameState, final Object[] args,
                          final CrewMember... crewMembers) {
        super.perform(gameState, args, crewMembers);
        if (crewMembers.length != 1) {
            throw new ActionException("Invalid args given");
        }
        crewMembers[0].restoreTiredness();
        return null;
    }
}
