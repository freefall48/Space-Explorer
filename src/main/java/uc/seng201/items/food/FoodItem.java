package uc.seng201.items.food;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.items.IItem;
import uc.seng201.items.Items;

/**
 * The base class for food items. Provides the base implementation
 * that they can override.
 */
public class FoodItem implements IItem {

    private int price;
    private int nutritionalValue;
    private Items itemType;

    public FoodItem(int price, int nutritionalValue, Items itemType) {
        this.price = price;
        this.nutritionalValue = nutritionalValue;
        this.itemType = itemType;
    }

    /**
     * Gets the nutritional value of the food.
     *
     * @return The nutritionalValue of the food.
     */
    public int getNutritionalValue() {
        return nutritionalValue;
    }

    /**
     * Gets the price of the food item that it can be sold at.
     *
     * @return Price of the FoodItem.
     */
    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void onUse(CrewMember crewMember, SpaceShip spaceShip) {
        if (!spaceShip.contains(itemType)) {
            return;
        }

        crewMember.alterFood(nutritionalValue);
        spaceShip.remove(itemType);
    }
}
