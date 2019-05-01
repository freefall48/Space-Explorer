package uc.seng201.events;

import uc.seng201.SpaceShip;

public class EventAsteroidBelt implements IRandomEvent {
    @Override
    public void onTrigger(SpaceShip spaceShip) {
        spaceShip.alterShield(-1);
    }
}
