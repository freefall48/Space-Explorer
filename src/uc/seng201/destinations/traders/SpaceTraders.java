package uc.seng201.destinations.traders;

import uc.seng201.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.utils.Helpers;
import uc.seng201.items.SpaceItem;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class SpaceTraders {

    private Map<SpaceItem, Integer> availableItems;

    public SpaceTraders() {
        NewItemsHandler newItemsHandler = new NewItemsHandler();
        SpaceExplorer.eventManager.addObserver(Event.START_DAY, newItemsHandler);
        SpaceExplorer.eventManager.addObserver(Event.NEW_GAME_STATE, newItemsHandler);
        SpaceExplorer.eventManager.addObserver(Event.BUY_FROM_TRADERS, new BuyFromTradersHandler());

        availableItems = new Hashtable<>();
    }


    public Map<SpaceItem, Integer> getAvailableItems() {
        return Collections.unmodifiableMap(availableItems);
    }

    private void generateAvailableItemsToday(boolean isFriendly) {

        int quantityToDisplay = Helpers.randomGenerator.nextInt(SpaceItem.values().length / 2) + 4;
        if (isFriendly) {
            quantityToDisplay += 10;
        }

        while (quantityToDisplay >= availableItems.size()) {
            SpaceItem item = SpaceItem.values()[Helpers.randomGenerator.nextInt(SpaceItem.values().length)];

            // Cannot use the primitive type here as we are dealing with nulls.
            Integer currentItemQuantity = availableItems.putIfAbsent(item, 1);
            if (currentItemQuantity != null) {
                availableItems.replace(item, currentItemQuantity + 1);
            }
            quantityToDisplay -= 1;
        }

    }

    class NewItemsHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof GameState) {
                    boolean isFriendly = false;
                    for (CrewMember crewMember : ((GameState) args[0]).getSpaceShip().getShipCrew()) {
                        if (crewMember.getModifications().contains(Modifications.FRIENDLY)) {
                            isFriendly = true;
                            break;
                        }
                    }
                    generateAvailableItemsToday(isFriendly);
                }
            }
        }
    }

    class BuyFromTradersHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof SpaceItem) {
                    SpaceItem item = (SpaceItem) args[0];
                    int currentQuantity = availableItems.get(item);
                    int newQuantity = currentQuantity - 1;
                    availableItems.replace(item, newQuantity);
                    if (newQuantity == 0) {
                        availableItems.remove(item);
                    }
                }
            }
        }
    }
}
