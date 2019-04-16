package uc.seng201.items.food;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.items.IItem;
import uc.seng201.items.ItemType;
import uc.seng201.items.NoItemException;

/**
 * The base class for food items. Provides the base implementation
 * that they can override.
 */
public class FoodItem implements IItem {

    private int price;
    private int nutritionalValue;
    private ItemType itemType;

    public FoodItem(int price, int nutritionalValue, ItemType itemType) {
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
     * @return  Price of the FoodItem.
     */
    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void onUse(CrewMember crewMember) {
        if (!SpaceExplorer.getSpaceShip().contains(itemType)) {
            throw new NoItemException();
        }

        crewMember.alterFood(nutritionalValue);
        SpaceExplorer.getSpaceShip().remove(itemType);
    }
}
