package uc.seng201.crew;

import uc.seng201.crew.modifers.Modifications;
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
    private int repairAmount;
    private final int maxTiredness = 100;
    private final int tirednessRate = 25;
    private int maxFoodLevel = 100;
    private int foodDecayRate = -20;
    private String name;
    private CrewType crewType;
    private int health;
    private int currentHealthRegen;
    private int tiredness;
    private int actionsLeftToday = 2;
    private List<Modifications> modifications = new ArrayList<>();
    private int foodLevel = maxFoodLevel;

    public CrewMember(String name, CrewType crewType, int maxHealth, int baseHealthRegen, int repairAmount) {
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = this.health = maxHealth;
        this.baseHealthRegen = this.currentHealthRegen = baseHealthRegen;
        this.repairAmount = repairAmount;
    }

    @Override
    public String toString() {
        return String.format("%s the %s has %d actions. ",
                this.name, this.crewType, this.actionsLeftToday) +
                String.format("[%d/%d %d/day] HP. [%d/%d %d/day] Food. [%d/%d %d/day] Tiredness. ",
                        this.health, this.maxHealth, this.currentHealthRegen, this.foodLevel,
                        this.maxFoodLevel, this.foodDecayRate, this.tiredness, this.maxTiredness, this.tirednessRate) +
                String.format("Modifiers [%s]", Helpers.listToString(this.modifications, false));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CrewMember) {
            return (((CrewMember) object).getName().toUpperCase().equals(this.name.toUpperCase()));
        }
        return false;
    }

    public int getTirednessRate() {
        return tirednessRate;
    }

    public int getMaxFoodLevel() {
        return maxFoodLevel;
    }

    public int getFoodDecayRate() {
        return foodDecayRate;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseHealthRegen() {
        return baseHealthRegen;
    }

    public int getRepairAmount() {
        return repairAmount;
    }

    public int getMaxTiredness() {
        return maxTiredness;
    }


    public CrewType getCrewType() {
        return crewType;
    }

    public int getHealth() {
        return health;
    }

    public int getCurrentHealthRegen() {
        return currentHealthRegen;
    }

    public int getTiredness() {
        return tiredness;
    }

    public int getActionsLeftToday() {
        return actionsLeftToday;
    }

    public List<Modifications> getModifications() {
        return modifications;
    }


    public int getFoodLevel() {
        return foodLevel;
    }

    public void setRepairAmount(int repairAmount) {
        this.repairAmount = repairAmount;
    }

    public void setMaxFoodLevel(int maxFoodLevel) {
        this.maxFoodLevel = maxFoodLevel;
    }

    public void setFoodDecayRate(int foodDecayRate) {
        this.foodDecayRate = foodDecayRate;
    }

    public void setHealthRegen(int regenRate) {
        this.currentHealthRegen = regenRate;
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
     * @param modification Modification to add to crew member
     */
    public void addModification(Modifications modification) {
        if (!this.modifications.contains(modification)) {
            this.modifications.add(modification);
            modification.getInstance().onAdd(this);
        }
    }

    /**
     * Removes an illness from a crew member. The Illnesses onRemove()
     * method is called.
     *
     * @param modification The Illness to remove from the crew member.
     */
    public void removeModification(Modifications modification) {
        if (this.modifications.contains(modification)) {
            this.modifications.remove(modification);
            modification.getInstance().onRemove(this);
        }
    }

    /**
     * Alters the current stats of the crew member by applying the given rates to them. Gives the
     * crew member actions for the day. Called at the start of every day for each crew member.
     */
    public void updateStats() {
        alterHealth(this.currentHealthRegen);
        alterFood(this.foodDecayRate);
        alterTiredness(this.tirednessRate);
        actionsLeftToday = 2;

        this.modifications.forEach(modification -> modification.getInstance().onTick(this));

    }

    public boolean canPerformActions() {
        return this.actionsLeftToday > 0;
    }

    public void performAction() {
        if (this.actionsLeftToday > 0) {
            this.actionsLeftToday -= 1;
        } else {
            throw new ActionException();
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }
}
