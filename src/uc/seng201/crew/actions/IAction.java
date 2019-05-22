package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;

/**
 * Interface for all crew actions.
 */
public interface IAction {

    /**
     * Called when a crew member performs the action.
     *
     * @param gameState reference to the current game state.
     * @param args arguments that are action specific.
     * @param crewMembers who is performing the action.
     * @return message describing the outcome of the action.
     */
    String perform(GameState gameState, Object[] args, CrewMember... crewMembers);
}
