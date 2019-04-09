package uc.seng201.logic.crew;

import uc.seng201.logic.food.FoodItem;
import uc.seng201.logic.medical.Illnesses;

import java.util.ArrayList;
import java.util.List;

/*
* Base class that all crew members extend.
*/
public class CrewMember {

    private String name;
    private CrewTypes crewType;

    int maxHealth;
    int health;
    int healthRegenRate;

    int tiredness;
    int tirednessRate;

    int actionsLeftToday = 2;

    List<Illnesses> illnesses = new ArrayList<>();

    private final int maxFoodLevel = 100;
    private int foodLevel = maxFoodLevel;
    int foodDecayRate;

    public CrewMember(String name, CrewTypes crewType) {
        this.name = name;
        this.crewType = crewType;
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

    public void addHealth(int health) {

        int newHealth = this.health + health;
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        }
        this.health = newHealth;
    }

    public boolean removeHealth(int health) {
        int newHealth = this.health - health;
        if (newHealth <= 0) {
            return false;
        }
        this.health = newHealth;
        return true;
    }

    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    public void setHealthRegenRate(int healthRegenRate) {
        if (healthRegenRate >= 0 && healthRegenRate <= 100) {
            this.healthRegenRate = healthRegenRate;
        } else {
            throw new CrewException();
        }
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    /*
     * Feed a crew member a FoodItem.
     */
    public void feed(FoodItem foodItem) {
        int foodLevel = this.foodLevel + foodItem.getFoodValue();
        if (foodLevel > maxHealth) {
            foodLevel = maxHealth;
        }
        this.foodLevel = foodLevel;
    }

    public int getFoodDecayRate() {
        return foodDecayRate;
    }

    public int getTiredness() {
        return tiredness;
    }


    public int getTirednessRate() {
        return tirednessRate;
    }


    public void addIllness(Illnesses illness) {
        this.illnesses.add(illness);
    }

    public void removeIllness(Illnesses illness) {
        this.illnesses.remove(illness);
    }

    public void performAction() {
        this.actionsLeftToday -= 1;
    }
}
