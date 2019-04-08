package uc.seng201.crew;

import uc.seng201.food.FoodItem;
import uc.seng201.medical.Illnesses;

import java.util.ArrayList;
import java.util.List;

/*
* Base class that all crew members extend.
*/
public class CrewMember {

    private String name;
    private CrewTypes crewType;

    private int maxHealth;
    private int health;
    private int healthRegenRate;

    private List<Illnesses> illnesses = new ArrayList<>();

    private int foodLevel;
    private int foodDecayRate;

    private int tiredness;
    private int tirednessRate;

    public CrewMember(String name, CrewTypes crewType) {
        this.name = name;
        this.crewType = crewType;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public CrewTypes getCrewType() {
        return crewType;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    public void setHealthRegenRate(int healthRegenRate) {
        this.healthRegenRate = healthRegenRate;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getFoodDecayRate() {
        return foodDecayRate;
    }

    public void setFoodDecayRate(int foodDecayRate) {
        this.foodDecayRate = foodDecayRate;
    }

    public int getTiredness() {
        return tiredness;
    }

    public void setTiredness(int tiredness) {
        this.tiredness = tiredness;
    }

    public int getTirednessRate() {
        return tirednessRate;
    }

    public void setTirednessRate(int tirednessRate) {
        this.tirednessRate = tirednessRate;
    }



    /*
    * Feed a crew member a FoodItem.
    */
    public void feed(FoodItem foodItem) {
        int newFoodLevel = foodLevel + foodItem.getFoodValue();
        if (newFoodLevel > maxHealth) {
            newFoodLevel = maxHealth;
        }
        foodLevel = newFoodLevel;
    }

    public void addIllness(Illnesses illness) {
        this.illnesses.add(illness);
    }

    public void removeIllness(Illnesses illness) {
        this.illnesses.remove(illness);
    }

    public boolean hasIllness(Illnesses illness) {
        if (this.illnesses.contains(illness)) {
            return true;
        } else {
            return false;
        }
    }
}
