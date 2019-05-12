package uc.seng201.crew;

import uc.seng201.crew.modifers.Modifications;
import uc.seng201.errors.CrewMemberException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The base class for crew members. All crew members must
 * extend this class.
 */
public class CrewMember {

    private String name;
    private CrewType crewType;
    private int maxHealth;
    private int baseHealthRegen;
    private int repairAmount;
    private int maxTiredness;
    private int tirednessRate;
    private int foodDecayRate;
    private int health;
    private int currentHealthRegen;
    private int tiredness;
    private int actionsLeftToday;
    private Set<Modifications> modifications;
    private int foodLevel;
    private int maxFoodLevel;


    public CrewMember(String name, CrewType crewType) {
        this(name, crewType, 100, 10, 1, 100, 25,
                20, new HashSet<>(), 100);
    }

    public CrewMember(String name, CrewType crewType, int maxHealth, int baseHealthRegen, int repairAmount,
                      int maxTiredness, int tirednessRate, int foodDecayRate, Set<Modifications> modifications,
                      int maxFoodLevel) {
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = health = maxHealth;
        this.baseHealthRegen = currentHealthRegen = baseHealthRegen;
        this.repairAmount = repairAmount;
        this.maxTiredness = maxTiredness;
        this.tirednessRate = tirednessRate;
        this.foodDecayRate = foodDecayRate;
        this.modifications = modifications;
        this.maxFoodLevel = foodLevel = maxFoodLevel;
        this.actionsLeftToday = 2;
    }

    @Override
    public String toString() {
        return String.format("%s the %s has %d actions. ",
                this.name, this.crewType, this.actionsLeftToday) +
                String.format("[%d/%d %d/day] HP. [%d/%d %d/day] Food. [%d/%d %d/day] Tiredness. ",
                        this.health, this.maxHealth, this.currentHealthRegen, this.foodLevel,
                        this.maxFoodLevel, this.foodDecayRate, this.tiredness, this.maxTiredness, this.tirednessRate) +
                String.format("Modifiers %s", modifications.toString());
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

    public Collection<Modifications> getModifications() {
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

    public void setActionsLeftToday(int actionsLeftToday) {
        this.actionsLeftToday = actionsLeftToday;
    }

    public void setHealthRegen(int regenRate) {
        this.currentHealthRegen = regenRate;
    }

    public int alterFood(int food) {
        int newLevel = this.foodLevel + food;
        if (newLevel >= this.maxFoodLevel) {
            newLevel = this.maxFoodLevel;
        } else if (newLevel <= 0) {
            newLevel = 0;
        }
        return this.foodLevel = newLevel;
    }

    /**
     * Adds health to crew member. Does not exceed the
     * maximum health the member can have.
     *
     * @param health The amount of health to give the crew member.
     */
    public int alterHealth(int health) {

        int newHealth = this.health + health;
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        } else if (newHealth <= 0) {
            newHealth = 0;
        }
        return this.health = newHealth;
    }

    public int alterTiredness(int tiredness) {
        int newValue = this.tiredness + tiredness;
        if (newValue > this.maxTiredness) {
            newValue = this.maxTiredness;
        } else if (newValue < 0) {
            newValue = 0;
        }
        return this.tiredness = newValue;
    }

    /**
     * Applies an illness to the crew member. The Illnesses onAdd() method
     * is called.
     *
     * @param modification Modification to add to crew member
     */
    public boolean addModification(Modifications modification) {
        boolean isAdded = modifications.add(modification);
        if (isAdded) {
            modification.getInstance().onAdd(this);
        }
        return isAdded;
    }

    /**
     * Removes an illness from a crew member. The Illnesses onRemove()
     * method is called.
     *
     * @param modification The Illness to remove from the crew member.
     */
    public boolean removeModification(Modifications modification) {
        boolean isRemoved = modifications.remove(modification);
        if (isRemoved) {
            modification.getInstance().onRemove(this);
        }
        return isRemoved;
    }

    /**
     * Alters the current stats of the crew member by applying the given rates to them. Gives the
     * crew member actions for the day. Called at the start of every day for each crew member.
     */


    public boolean canPerformActions() {
        return this.actionsLeftToday > 0;
    }

    public void performAction() {
        if (this.actionsLeftToday > 0) {
            this.actionsLeftToday -= 1;
        } else {
            throw new CrewMemberException();
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }
}
