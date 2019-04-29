package uc.seng201.items;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Illnesses;

public enum Items{
    STARDEW(ItemType.MEDICAL,10,"Removes space plague from a crew member.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.removeIllness(Illnesses.SPACE_PLAGUE);
        }
    },
    CONTACTS(ItemType.MEDICAL,10,"Removes blindness from a crew member") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterHealth(10);
        }
    },

    PORK(ItemType.FOOD, 15,"Yummy yummy pork.") {
        @Override
        public void onConsume(CrewMember crewMember) {
            crewMember.alterFood(50);
        }
    };

    private ItemType itemType;
    private int price;
    private String itemDescription;

    Items(ItemType itemType, int price, String itemDescription) {
        this.itemType = itemType;
        this.itemDescription = itemDescription;
        this.price = price;
    }

    public abstract void onConsume(CrewMember crewMember);

    public ItemType getItemType() {
        return itemType;
    }

    public int getPrice() {return price;}

    public String getItemDescription() {
        return this.itemDescription;
    }
}
