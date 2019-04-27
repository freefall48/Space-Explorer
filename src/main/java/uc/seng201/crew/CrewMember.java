package uc.seng201.crew;

import uc.seng201.crew.modifers.Abilities;
import uc.seng201.crew.modifers.Illnesses;
import uc.seng201.errors.ActionException;
import uc.seng201.helpers.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for crew members. All crew members must
 * extend this class.
 */
public class CrewMember {

    private final int maxHealth;
    private final int baseHealthRegen;
    private final int repairAmount;
    private final int maxTiredness = 100;
    private final int tirednessRate = 25;
    private final int maxFoodLevel = 100;
    private final int foodDecayRate = 20;
    private String name;
    private CrewType crewType;
    private int health;
    private int currentHealthRegen;
    private int tiredness;
    private int actionsLeftToday = 2;
    private List<Illnesses> illnesses = new ArrayList<>();
    private List<Abilities> abilities = new ArrayList<>();
    private int foodLevel = maxFoodLevel;

    public CrewMember(String name, CrewType crewType, int maxHealth, int baseHealthRegen, int repairAmount) {
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = this.health = maxHealth;
        this.baseHealthRegen = baseHealthRegen;
        this.repairAmount = repairAmount;
    }

    @Override
    public String toString() {
        return String.format("%s, a %s that has %d actions left today, ",
                this.name, this.crewType, this.actionsLeftToday) +
                String.format("%d|%d health with regen of %d/pt, %d|%d food decaying " +
                                "at %d/pt and %d|%d tiredness at %d/pt. ",
                        this.health, this.maxHealth, this.currentHealthRegen, this.foodLevel,
                        this.maxFoodLevel, this.foodDecayRate, this.tiredness, this.maxTiredness, this.tirednessRate) +
                String.format("They have abilities '%s', but illnesses '%s'",
                        Helpers.listToString(this.abilities, true), Helpers.listToString(this.illnesses, true));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CrewMember) {
            return (((CrewMember) object).getName().toUpperCase().equals(this.name.toUpperCase()));
        }
        return false;
    }

    public String getName() {
        return this.name;
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
     * @param health The amount of health to give the crew member.
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
     * @param illness The Illness to apply to the crew member.
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
     * @param illness The Illness to remove from the crew member.
     */
    public void removeIllness(Illnesses illness) {
        this.illnesses.remove(illness);
        this.currentHealthRegen = this.baseHealthRegen;
    }

    /**
     * Alters the current stats of the crew member by applying the given rates to them. Gives the
     * crew member actions for the day. Called at the start of every day for each crew member.
     */
    public void updateStats() {
        if (this.abilities.contains(Abilities.HEALING_HANDS)) {
            alterHealth((int) (this.currentHealthRegen * 1.25));
        } else {
            alterHealth(this.currentHealthRegen);
        }
        alterFood(this.foodDecayRate);
        alterTiredness(this.tirednessRate);
        if (this.foodLevel == 0 || this.tiredness == 100) {
            this.actionsLeftToday = 0;
        } else {
            this.actionsLeftToday = 2;
        }

    }

    public boolean isAlive() {
        if (this.health > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canPerformActions() {
        if (this.actionsLeftToday > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void performAction() {
        if (this.actionsLeftToday > 0) {
            this.actionsLeftToday -= 1;
        } else {
            throw new ActionException();
        }
    }
}
