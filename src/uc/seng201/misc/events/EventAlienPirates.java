package uc.seng201.misc.events;

import uc.seng201.SpaceExplorer;
import uc.seng201.environment.GameState;
import uc.seng201.items.SpaceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Alien Pirates random event that can trigger at the start of a new day. Checks if there are
 * items for them to take. If there are then one is chosen randomly to be removed
 * from the space ships items.
 */
public class EventAlienPirates implements IRandomEvent {
    @Override
    public String onTrigger(GameState gameState) {
        if (gameState.getSpaceShip().getShipItems().size() > 0) {
            int positionToRemove = SpaceExplorer.randomGenerator.nextInt(gameState.getSpaceShip().getShipItems().size());
            List<SpaceItem> items = new ArrayList<>(gameState.getSpaceShip().getShipItems().keySet());
            SpaceItem takenItem = items.get(positionToRemove);
            gameState.getSpaceShip().remove(takenItem);
            return String.format("Alien pirates have boarded %s and taken %s!", gameState.getSpaceShip().getShipName(),
                    takenItem.toString());
        } else {
            // Its possible they attack when there are no items.
            return String.format("Alien pirates boarded %s but found nothing of value...",
                    gameState.getSpaceShip().getShipName());
        }
    }
}
