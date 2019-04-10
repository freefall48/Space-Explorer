package uc.seng201.logic.medical;

import uc.seng201.logic.crew.CrewMember;

/**
 * Base class for medical items.
 */
public class MedicalItem {

    int price;

    /**
     * Gets the current price of the medical item.
     *
     * @return Price of the medical item.
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Method to call when the onUse affects need to be applied to a
     * crew member.
     *
     * @param crewMember Target crew member.
     */
    public void onUse(CrewMember crewMember) {}
}
