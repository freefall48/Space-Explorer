package uc.seng201.items.medical;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Illnesses;
import uc.seng201.items.IItem;
import uc.seng201.items.ItemType;
import uc.seng201.items.NoItemException;

/**
 * Base class for medical items.
 */
public class MedicalItem implements IItem {

    private int price;
    private int healthValue;
    private Illnesses illnessToCure;
    private ItemType itemType;

    public MedicalItem(int price, int healthValue, Illnesses illnessToCure, ItemType itemType) {
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
    public void onUse(CrewMember crewMember) {
        if (!SpaceExplorer.getSpaceShip().contains(itemType)) {
            throw new NoItemException();
        }
        if (healthValue > 0) {
            crewMember.alterHealth(healthValue);
        }
        if (illnessToCure != null) {
            crewMember.removeIllness(illnessToCure);
        }
        SpaceExplorer.getSpaceShip().remove(itemType);
    }
}
