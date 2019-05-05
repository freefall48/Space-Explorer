package uc.seng201.destinations.traders;

import uc.seng201.items.Items;

public class TradersListing {

    private int quantity;
    private Items item;

    public TradersListing(int quantity, Items item) {
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

    public Items getItem() {
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
