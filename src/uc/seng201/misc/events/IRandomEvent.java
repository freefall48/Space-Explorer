package uc.seng201.misc.events;

import uc.seng201.environment.GameState;

/**
 * Interface for when to trigger a random event.
 */
public interface IRandomEvent {

    /**
     * Called when a random event is triggered.
     *
     * @param gameState reference to the game state.
     * @return description of changes made by the event.
     */
    String onTrigger(GameState gameState);

}
