package uc.seng201.events;

import uc.seng201.SpaceShip;
import uc.seng201.helpers.Helpers;

public class EventAlienPirates implements IRandomEvent {
    @Override
    public void onTrigger(SpaceShip spaceShip) {
        if (spaceShip.getShipItems().size() > 0) {
            int positionToRemove = Helpers.randomGenerator.nextInt(spaceShip.getShipItems().size());
            spaceShip.getShipItems().remove(positionToRemove);
        }
    }
}
