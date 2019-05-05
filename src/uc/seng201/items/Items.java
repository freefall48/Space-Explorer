package uc.seng201.items;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Modifications;

public enum Items{
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
