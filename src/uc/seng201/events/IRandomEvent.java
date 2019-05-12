package uc.seng201.events;

import uc.seng201.GameState;

public interface IRandomEvent {

    String onTrigger(GameState gameState);

}
