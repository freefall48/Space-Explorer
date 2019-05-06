package uc.seng201.destinations.traders;

import uc.seng201.items.SpaceItem;

public class TradersListing {

    private int quantity;
    private SpaceItem item;

    public TradersListing(int quantity, SpaceItem item) {
        this.quantity = quantity;
        this.item = item;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TradersListing) {
            return ((TradersListing) obj).getItem().equals(this.item);
        }
        return false;
    }

    public int getQuantity() {
        return quantity;
    }

    public SpaceItem getItem() {
        return item;
    }

    public void addOne() {
        this.quantity += 1;
    }

    public boolean isOneRemaining() {
        return this.quantity > 0;
    }

    public void removeOne() {
        if (isOneRemaining()) {
            this.quantity -= 1;
        } else {
            throw new IllegalStateException();
        }
    }
}
