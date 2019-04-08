package uc.seng201.logic.food;

public class FoodItem {

    int price;
    int foodValue;

    public int getFoodValue() {
        return foodValue;
    }

    public int getPrice() {
        return price;
    }

    protected void setPrice(int price) {
        this.price = price;
    }

    protected void setFoodValue(int foodValue) {
        this.foodValue = foodValue;
    }
}
