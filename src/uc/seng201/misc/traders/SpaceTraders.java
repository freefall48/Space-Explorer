package uc.seng201.misc.traders;

import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.items.SpaceItem;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * SpaceTraders provide the ability to buy items. What they are selling each day
 * is random.
 */
public class SpaceTraders {

    /**
     * The items that are available to buy today.
     */
    private Map<SpaceItem, Integer> availableItems;

    /**
     * Adds the event listeners for the SpaceTraders to the event manager and initialises
     * the available items map. Helps Gson when creating instances.
     */
    public SpaceTraders() {
        NewItemsHandler newItemsHandler = new NewItemsHandler();
        GameEnvironment.eventManager.addObserver(Event.START_DAY, newItemsHandler);
        GameEnvironment.eventManager.addObserver(Event.NEW_GAME_STATE, newItemsHandler);
        GameEnvironment.eventManager.addObserver(Event.BUY_FROM_TRADERS, new BuyFromTradersHandler());

        availableItems = new Hashtable<>();
    }


    /**
     * Returns an unmodifiable view of the items that the space traders are selling today. Direct changes
     * to the availableItems will be reflected in the view.
     *
     * @return Map of items available today.
     */
    public Map<SpaceItem, Integer> getAvailableItems() {
        return Collections.unmodifiableMap(availableItems);
    }

    /**
     * Generates the available items for sale. Removes all existing items then randomly adds new ones.
     * If a crew member has the modifier "Friendly" then the number of items that will be generated
     * is increased.
     *
     * @param isFriendly true if a crew member has the "Friendly" modifier.
     */
    private void generateAvailableItems(boolean isFriendly) {
        availableItems.clear();
        int quantityToDisplay = SpaceExplorer.randomGenerator.nextInt(SpaceItem.values().length / 2) + 4;
        if (isFriendly) {
            quantityToDisplay += 10;
        }

        while (quantityToDisplay >= availableItems.size()) {
            SpaceItem item = SpaceItem.values()[SpaceExplorer.randomGenerator.nextInt(SpaceItem.values().length)];

            // Cannot use the primitive type here as we are dealing with nulls.
            Integer currentItemQuantity = availableItems.putIfAbsent(item, 1);
            if (currentItemQuantity != null) {
                availableItems.replace(item, currentItemQuantity + 1);
            }
            quantityToDisplay -= 1;
        }

    }

    /**
     * SpaceTraders handler for the events "NEXT_DAY" and "NEW_GAME_STATE". Checks
     * if any crew members have the "FRIENDLY" modifier then generates the available
     * items.
     */
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
                    generateAvailableItems(isFriendly);
                }
            }
        }
    }

    /**
     * SpaceTraders handler for the event "BUY_FROM_TRADERS". Decreases the quantity of the item purchased by
     * one and if there are no more of that item, removes it from the list of available items.
     */
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
