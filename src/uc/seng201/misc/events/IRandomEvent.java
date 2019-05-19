package uc.seng201.misc.events;

import uc.seng201.environment.GameState;

public interface IRandomEvent {

    String onTrigger(GameState gameState);

}
