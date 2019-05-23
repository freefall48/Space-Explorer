package uc.seng201.items;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;

/**
 * Contains the items that are available in the game.
 * @author Matthew Johnson
 *
 */
public enum SpaceItem {
    /**
     * Carrots are a food item that restore 10 food for $5.
     */
    CARROT(ItemType.FOOD, 5, "Increase food by 10."),
    /**
     * Chicken is a food item that restores 30 food for $15.
     */
    CHICKEN(ItemType.FOOD, 15, "Time for roast. Increases food by 30."),
    /**
     * Crackers are a food item that restores 20 food for $10.
     */
    CRACKERS(ItemType.FOOD, 10, "Increases food by 20."),
    /**
     * Crickets are a food item that restores 10 food for $5.
     */
    CRICKETS(ItemType.FOOD, 5, "Increases food by 10."),
    /**
     * Pork is a food item that restores 60 food $30.
     */
    PORK(ItemType.FOOD, 30, "Yummy yummy pork. Increases food by 60."),
    /**
     * Space snack is a food item that restores 20 food for $10.
     */
    SPACESNACK(ItemType.FOOD, 10, "Not sure what this is? Increases food by 20."),

    /**
     * Bandage is a medical item that restores 50 health for $30.
     */
    BANDAGE(ItemType.MEDICAL, 30, "Increases a crew members health by 50.") {
        /**
         * When consumed alter the crew members health.
         * @param crewMember to affect
         */
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterHealth(50);
        }
    },
    /**
     * Stardew is a medical item that removes space plague from a crew member for $15.
     */
    STARDEW(ItemType.MEDICAL, 15, "Removes space plague from a crew member.") {
        /**
         * When consumed remove the space plague from the crew member.
         * @param crewMember to affect
         */
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.removeModification(Modifications.SPACE_PLAGUE);
        }
    },
    /**
     * Oinkment is a medical item that restores 20 health for $12.
     */
    OINKMENT(ItemType.MEDICAL, 12, "Increases a crew members health by 20") {
        /**
         * When consumed alter the crew members health.
         * @param crewMember to affect
         */
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterHealth(20);
        }
    };

    /**
     * The type of the item.
     */
    private ItemType itemType;
    /**
     * The price that the item costs. This normally influences the
     * strength of its onConsume method.
     */
    private int price;
    /**
     * Message to be used to describe the item.
     */
    private String itemDescription;

    /**
     * Creates a reference item that is available throughout the code.
     *
     * @param itemType the type of item.
     * @param price cost of the item.
     * @param itemDescription message to describe the item.
     */
    SpaceItem(ItemType itemType, int price, String itemDescription) {
        this.itemType = itemType;
        this.itemDescription = itemDescription;
        this.price = price;
    }

    /**
     * Override this method to alter the behavior that occurs when the items
     * onConsume method is called. This method should be called whenever an
     * item is consumed by a crew member. The method has access to a mutable
     * crewMember who the method should apply effects too.
     * Defaults to altering the food level of the crew member by 2 times the
     * price of the item.
     *
     * @param crewMember to affect
     */
    public void onConsume(CrewMember crewMember) {
        crewMember.alterFood(price * 2);
    }

    /**
     * Returns the item type of the item.
     *
     * @return item type.
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Returns the price of the item.
     *
     * @return price of the item.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns a description of the item.
     *
     * @return item description.
     */
    public String getItemDescription() {
        return this.itemDescription;
    }
}
