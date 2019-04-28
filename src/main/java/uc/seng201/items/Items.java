package uc.seng201.items;

public enum Items {
    STARDEW(ItemType.MEDICAL,"Removes space plague from a crew member."),
    CONTACTS(ItemType.MEDICAL,"Removes blindness from a crew member");

    private ItemType itemType;
    private String itemDescription;

    Items(ItemType itemType, String itemDescription) {
        this.itemType = itemType;
        this.itemDescription = itemDescription;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getItemDescription() {
        return this.itemDescription;
    }
}
