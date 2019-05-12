package uc.seng201.events;

import java.util.ArrayList;
import java.util.List;

import uc.seng201.SpaceShip;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.SpaceItem;

public class EventAlienPirates implements IRandomEvent {
    @Override
    public void onTrigger(SpaceShip spaceShip) {
        if (spaceShip.getShipItems().size() > 0) {
            int positionToRemove = Helpers.randomGenerator.nextInt(spaceShip.getShipItems().size());
            List<SpaceItem> items = new ArrayList<>(spaceShip.getShipItems().keySet());
            SpaceItem takenItem = items.get(positionToRemove);
            spaceShip.remove(takenItem);
        }
    }
}
