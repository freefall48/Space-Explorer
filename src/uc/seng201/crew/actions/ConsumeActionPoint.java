package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;

/**
 * Implements IAction and reduces each acting crew members actions
 * remaining by one. Actions that require actions points should
 * extend this class.
 */
public abstract class ConsumeActionPoint implements IAction {

    /**
     * Reduce all acting crew members action points by 1.
     *
     * @param gameState   reference to the current game state.
     * @param args        arguments that are action specific.
     * @param crewMembers who is performing the action.
     * @return null.
     */
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        for (CrewMember crewMember : crewMembers) {
            crewMember.performAction();
        }
        return null;
    }
}
