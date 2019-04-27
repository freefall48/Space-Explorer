package uc.seng201.targets.events;

import uc.seng201.SpaceExplorer;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.ItemType;

import java.util.List;

public class SpacePirates implements IEvent {


    @Override
    public void onTriggered() {
        List<ItemType> currentItems = SpaceExplorer.getSpaceShip().getShipItems();
        int positionToRemove = Helpers.randomGenerator.nextInt(currentItems.size());
        currentItems.remove(positionToRemove);
    }
}
