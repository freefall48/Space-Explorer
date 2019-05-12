package uc.seng201.items;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;

/**
 * Contains the items that are available in the game.
 * @author Matthew Johnson
 *
 */
public enum SpaceItem {
    STARDEW(ItemType.MEDICAL,10,"Removes space plague from a crew member.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.removeModification(Modifications.SPACE_PLAGUE);
        }
    },
    CONTACTS(ItemType.MEDICAL,10,"Removes blindness from a crew member") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterHealth(getPrice());
        }
    },
    PORK(ItemType.FOOD, 15,"Yummy yummy pork.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
        }
    },
    CRICKETS(ItemType.FOOD, 15,"Bear would do it.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
        }
    },
    CRACKERS(ItemType.FOOD, 15,"now what flavor?") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
        }
    },
    CARROT(ItemType.FOOD, 5,"At least its healthy...") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
        }
    },
    CHICKEN(ItemType.FOOD, 15,"Time for roast.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
        }
    },
    SPACESNACK(ItemType.FOOD, 15,"Not sure what this is?") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(getPrice()*2);
            crewMember.setHealthRegen(crewMember.getBaseHealthRegen());
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
     * 
     * @param crewMember
     */
    public abstract void onConsume(CrewMember crewMember);
    
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
    public int getPrice() {return price;}

    /**
     * Returns a description of the item.
     * 
     * @return item description.
     */
    public String getItemDescription() {
        return this.itemDescription;
    }
}
