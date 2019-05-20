package uc.seng201.misc.events;

import uc.seng201.environment.GameState;

/**
 * Random event that can be triggered when the Action PILOT is performed. Removes one from the
 * health of the spaceship.
 */
public class EventAsteroidBelt implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        gameState.getSpaceShip().alterShield(-1);
        return String.format("%s flew through an asteroid belt and taken damage!",
                gameState.getSpaceShip().getShipName());
    }
}
