package uc.seng201.items.medical;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Illnesses;
import uc.seng201.items.IItem;
import uc.seng201.items.Items;

/**
 * Base class for medical items.
 */
public class MedicalItem implements IItem {

    private int price;
    private int healthValue;
    private Illnesses illnessToCure;
    private Items itemType;

    public MedicalItem(int price, int healthValue, Illnesses illnessToCure, Items itemType) {
        this.price = price;
        this.healthValue = healthValue;
        this.illnessToCure = illnessToCure;
        this.itemType = itemType;
    }

    /**
     * Gets the current price of the medical item.
     *
     * @return Price of the medical item.
     */
    @Override
    public int getPrice() {
        return this.price;
    }

    /**
     * Method to call when the onUse affects need to be applied to a
     * crew member.
     *
     * @param crewMember Target crew member.
     */
    @Override
    public void onUse(CrewMember crewMember, SpaceShip spaceShip) {
        if (!spaceShip.contains(itemType)) {
            return;
        }
        if (healthValue > 0) {
            crewMember.alterHealth(healthValue);
        }
        if (illnessToCure != null) {
            crewMember.removeIllness(illnessToCure);
        }
        spaceShip.remove(itemType);
    }
}
