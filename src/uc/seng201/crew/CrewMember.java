package uc.seng201.crew;

import uc.seng201.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for crew members. All crew members must
 * extend this class.
 */
public class CrewMember {

    private String name;
    private CrewType crewType;

    private final int maxHealth;
    private int health;
    private final int baseHealthRegen;
    private int currentHealthRegen;

    private final int repairAmount;

    private final int maxTiredness = 100;
    private int tiredness;
    private final int tirednessRate = 25;

    private int actionsLeftToday = 2;

    private List<Illnesses> illnesses = new ArrayList<>();
    private List<Abilities> abilities = new ArrayList<>();


    private final int maxFoodLevel = 100;
    private int foodLevel = maxFoodLevel;
    private final int foodDecayRate = 20;

    @Override
    public String toString() {
        return String.format("Crew member %s is a %s and has %d actions left today. " +
                        "They have %d health, %d food and at %d tiredness. " +
                        "They have %s ability, but %s illness.",
                name, crewType, actionsLeftToday, health, foodLevel, tiredness,
                Helpers.listToString(abilities), Helpers.listToString((illnesses)));
    }

    public CrewMember(String name, CrewType crewType, int maxHealth, int baseHealthRegen, int repairAmount) {
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = this.health = maxHealth;
        this.baseHealthRegen = baseHealthRegen;
        this.repairAmount = repairAmount;
    }

    public void alterFood(int food) {
        int newLevel = this.foodLevel + food;
        if (newLevel >= this.maxFoodLevel) {
            newLevel = this.maxFoodLevel;
        } else if (newLevel <= 0) {
            newLevel = 0;
        }
        this.foodLevel = newLevel;
    }

    /**
     * Adds health to crew member. Does not exceed the
     * maximum health the member can have.
     *
     * @param health    The amount of health to give the crew member.
     *
     */
    public void alterHealth(int health) {

        int newHealth = this.health + health;
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        } else if (newHealth <= 0) {
            this.health = 0;
        }
        this.health = newHealth;
    }

    public void alterTiredness(int tiredness) {
        int newValue = this.tiredness + tiredness;
        if (newValue > this.maxTiredness) {
            newValue = this.maxTiredness;
        } else if (newValue < 0) {
            newValue = 0;
        }
        this.tiredness = newValue;
    }

    /**
     * Applies an illness to the crew member. The Illnesses onAdd() method
     * is called.
     *
     * @param illness   The Illness to apply to the crew member.
     */
    public void addIllness(Illnesses illness) {
        this.illnesses.add(illness);
        switch (illness) {
            case SPACE_PLAGUE:
                this.currentHealthRegen = 0;
                break;
            case SPACE_PARASITE:
                this.currentHealthRegen = -10;
                break;
        }
    }

    /**
     * Removes an illness from a crew member. The Illnesses onRemove()
     * method is called.
     *
     * @param illness   The Illness to remove from the crew member.
     */
    public void removeIllness(Illnesses illness) {
        this.illnesses.remove(illness);
        this.currentHealthRegen = this.baseHealthRegen;
    }

    /**
     * Alters the current stats of the crew member by applying the given rates to them.
     */
    public void updateStats() {
        if (this.abilities.contains(Abilities.HEALING_HANDS)) {
            alterHealth((int) (this.currentHealthRegen * 1.25));
        } else {
            alterHealth(this.currentHealthRegen);
        }
        alterFood(this.foodDecayRate);
        alterTiredness(this.tirednessRate);

    }

    public boolean performAction() {
        if (this.actionsLeftToday > 0) {
            this.actionsLeftToday -= 1;
            return true;
        } else {
            return false;
        }
    }
}
