package uc.seng201.misc.events;

import uc.seng201.environment.GameState;

public class EventAsteroidBelt implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        gameState.getSpaceShip().alterShield(-1);
        return String.format("%s flew through an asteroid belt and taken damage!",
                gameState.getSpaceShip().getShipName());
    }
}
