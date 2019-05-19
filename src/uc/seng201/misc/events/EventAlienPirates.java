package uc.seng201.misc.events;

import java.util.ArrayList;
import java.util.List;

import uc.seng201.environment.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.items.SpaceItem;

/**
 * Alien Pirates random event.
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
            return String.format("Alien pirates boarded %s but found nothing of value...",
                    gameState.getSpaceShip().getShipName());
        }
    }
}
