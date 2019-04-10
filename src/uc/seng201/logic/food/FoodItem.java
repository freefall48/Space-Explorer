package uc.seng201.logic.food;

/**
 * The base class for food items. Provides the base implementation
 * that they can override.
 */
public class FoodItem {

    int price;
    int nutritionalValue;

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
    public int getPrice() {
        return price;
    }
}
