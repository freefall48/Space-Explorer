package uc.seng201.destinations.traders;

import uc.seng201.utils.Helpers;
import uc.seng201.items.SpaceItem;

import java.util.ArrayList;
import java.util.List;

public class SpaceTraders {

    private List<TradersListing> availableItems;

    public List<TradersListing> getAvailableItems() {
        return availableItems;
    }

    public void generateAvailableItemsToday(boolean friendly) {
        availableItems = new ArrayList<>();
        int quantityToDisplay = Helpers.randomGenerator.nextInt(SpaceItem.values().length / 2) + 4;
        if (friendly) {
            quantityToDisplay += 10;
        }
        while (quantityToDisplay > 0) {
            int itemId = Helpers.randomGenerator.nextInt(SpaceItem.values().length);
            TradersListing newListing = new TradersListing(1, SpaceItem.values()[itemId]);
            if (this.availableItems.contains(newListing)) {
                int currentPosition = this.availableItems.indexOf(newListing);
                TradersListing currentListing = this.availableItems.get(currentPosition);
                currentListing.addOne();
                this.availableItems.set(currentPosition, currentListing);
            } else {
                this.availableItems.add(newListing);
            }
            quantityToDisplay -= 1;
        }

    }
}
