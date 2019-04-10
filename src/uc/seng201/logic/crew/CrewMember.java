package uc.seng201.logic.crew;

import uc.seng201.logic.food.FoodItem;
import uc.seng201.logic.medical.Illnesses;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for crew members. All crew members must
 * extend this class.
 */
public class CrewMember {

    private String name;
    private CrewType crewType;

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

    /**
     * Constructor that creates a crew member with a name and type.
     *
     * @param name      The name of this crew member.
     * @param crewType  The CrewType of this crew member.
     */
    public CrewMember(String name, CrewType crewType) {
        this.name = name;
        this.crewType = crewType;
    }

    /**
     * Gets the name of the crew member.
     *
     * @return Name of the crew member.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the CrewType of the crew member.
     *
     * @return CrewType of the crew member.
     */
    public CrewType getCrewType() {
        return crewType;
    }

    /**
     * Gets the current health of the crew member.
     *
     * @return Health of the current crew member.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Adds health to crew member. Does not exceed the
     * maximum health the member can have.
     *
     * @param health    The amount of health to give the crew member.
     */
    public void addHealth(int health) {

        int newHealth = this.health + health;
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        }
        this.health = newHealth;
    }

    /**
     * Removes health from the crew member and checks if they
     * are still alive.
     *
     * @param health    the amount of health to remove from the crew member.
     * @return          Whether the crew member is still alive
     */
    public boolean removeHealth(int health) {
        int newHealth = this.health - health;
        if (newHealth <= 0) {
            return false;
        }
        this.health = newHealth;
        return true;
    }

    /**
     * Gets the healthRegenRate for the crew member.
     *
     * @return  The rate at which the crew member regenerates health.
     */
    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    /**
     * Gets the current foodLevel for the crew member.
     *
     * @return The current food level of the crew member.
     */
    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Feeds the crew member a FoodItem. Checks to make sure the maximum
     * food level is not exceeded.
     *
     * @param foodItem  FoodItem that is to be applied to the crew member.
     */
    public void feed(FoodItem foodItem) {
        int foodLevel = this.foodLevel + foodItem.getFoodValue();
        if (foodLevel > maxFoodLevel) {
            foodLevel = maxFoodLevel;
        }
        this.foodLevel = foodLevel;
    }

    /**
     * Gets the foodDecayRate for the crew member.
     *
     * @return The rate at which the foodLevel should be decremented.
     */
    public int getFoodDecayRate() {
        return foodDecayRate;
    }

    /**
     * Gets the current tiredness of the crew member.
     *
     * @return The current tiredness level of the crew member.
     */
    public int getTiredness() {
        return tiredness;
    }

    /**
     * Gets the tirednessRate for the crew member.
     *
     * @return The rate at which tiredness should increment.
     */
    public int getTirednessRate() {
        return tirednessRate;
    }

    /**
     * Applies an illness to the crew member. The Illnesses onAdd() method
     * is called.
     *
     * @param illness   The Illness to apply to the crew member.
     */
    public void addIllness(Illnesses illness) {
        this.illnesses.add(illness);
    }

    /**
     * Removes an illness from a crew member. The Illnesses onRemove()
     * method is called.
     *
     * @param illness   The Illness to remove from the crew member.
     */
    public void removeIllness(Illnesses illness) {
        this.illnesses.remove(illness);
    }

    /**
     * Performs an action as the crew member and decreases
     * the number of actions the member has.
     * @throws UnsupportedOperationException    Not currently implemented.
     */
    public void performAction() {
        throw new UnsupportedOperationException();
    }
}
