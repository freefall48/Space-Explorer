package uc.seng201.crew;

import uc.seng201.Display;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.modifers.Modifications;
import uc.seng201.errors.CrewMemberException;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.util.Collection;
import java.util.Collections;
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
    private int currentTirednessRate;
    private int baseTirednessRate;
    private int currentFoodDecayRate;
    private int baseFoodDecayRate;
    private int health;
    private int currentHealthRegen;
    private int tiredness;
    private int actionsLeftToday;
    private Set<Modifications> modifications;
    private int foodLevel;
    private int maxFoodLevel;


    private CrewMember() {
        SpaceExplorer.eventManager.addObserver(Event.START_DAY, new NextDay());
    }

    public CrewMember(String name, CrewType crewType) {
        this(name, crewType, 100, 10, 1, 100, 25,
                -20, new HashSet<>(), 100);
    }

    public CrewMember(String name, CrewType crewType, int maxHealth, int healthRegen, int repairAmount,
                      int maxTiredness, int tirednessRate, int foodDecayRate, Set<Modifications> modifications,
                      int maxFoodLevel) {
        this();
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = health = maxHealth;
        this.baseHealthRegen = currentHealthRegen = healthRegen;
        this.repairAmount = repairAmount;
        this.maxTiredness = maxTiredness;
        this.currentTirednessRate = baseTirednessRate = tirednessRate;
        this.currentFoodDecayRate = baseFoodDecayRate = foodDecayRate;
        this.modifications = modifications;
        this.maxFoodLevel = foodLevel = maxFoodLevel;
        this.actionsLeftToday = 2;

    }

    @Override
    public String toString() {
        return String.format("%s - %s", name, crewType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CrewMember) {
            CrewMember crewMember = (CrewMember) obj;
            return crewMember.getName().equals(name) && crewMember.getCrewType().equals(crewType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (name + crewType).hashCode();
    }

    public String description() {
        return String.format("%s the %s has %d actions. ",
                this.name, this.crewType, this.actionsLeftToday) +
                String.format("[%d/%d %d/day] HP. [%d/%d %d/day] Food. [%d/%d %d/day] Tiredness. ",
                        this.health, this.maxHealth, this.currentHealthRegen, this.foodLevel,
                        this.maxFoodLevel, this.currentFoodDecayRate, this.tiredness, this.maxTiredness, this.currentTirednessRate);
    }

    public int getTirednessRate() {
        return currentTirednessRate;
    }

    public int getMaxFoodLevel() {
        return maxFoodLevel;
    }

    public int getFoodDecayRate() {
        return currentFoodDecayRate;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxHealth() {
        return maxHealth;
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

    public int getHealthRegen() {
        return currentHealthRegen;
    }

    public int getTiredness() {
        return tiredness;
    }

    public int getActionsLeftToday() {
        return actionsLeftToday;
    }

    public Collection<Modifications> getModifications() {
        return Collections.unmodifiableSet(modifications);
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setRepairAmount(int repairAmount) {
        this.repairAmount = repairAmount;
    }

    public void setHealthRegen(int regenRate) {
        this.currentHealthRegen = regenRate;
    }

    public void restoreHealthRegen() {
        currentHealthRegen = baseHealthRegen;
    }

    public void alterActionsLeft(int value) {
        int newActionsLeft = actionsLeftToday + value;
        if (value < 0) {
            newActionsLeft = 0;
        }
        actionsLeftToday = newActionsLeft;
    }
    public void alterHealthRegen(int value) {
        currentHealthRegen += value;
    }

    public void alterFood(int food) {
        int newLevel = this.foodLevel + food;
        if (newLevel >= this.maxFoodLevel) {
            newLevel = this.maxFoodLevel;
        } else if (newLevel <= 0) {
            newLevel = 0;
        }
        foodLevel = newLevel;
    }

    public void restoreFoodDecayRate() {
        currentFoodDecayRate = baseFoodDecayRate;
    }
    public void alterFoodDecayRate(int value) {
        int newFoodDecayRate = currentFoodDecayRate + value;
        if (newFoodDecayRate < 0) {
            newFoodDecayRate = 0;
        }
        currentFoodDecayRate = newFoodDecayRate;
    }

    public void restoreTirednessRate() {
        currentTirednessRate = baseTirednessRate;
    }

    public void alterTirednessRate(int value) {
        int newTirednessRate = currentTirednessRate + value;
        if (newTirednessRate < 0) {
            newTirednessRate = 0;
        }
        currentTirednessRate = newTirednessRate;
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
            newHealth = 0;
        }
        this.health = newHealth;
    }

    public void restoreTiredness() {
        tiredness = 0;
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
        if (modifications.add(modification)) {
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
        if (modifications.remove(modification)) {
            modification.getInstance().onRemove(this);
        }
    }

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

    boolean isAlive() {
        return this.health > 0;
    }

    class NextDay implements Observer {

        @Override
        public void onEvent(Object... args) {
            if (getFoodLevel() == 0) {
                addModification(Modifications.HUNGRY);
            } else if (modifications.contains(Modifications.HUNGRY)) {
                removeModification(Modifications.HUNGRY);
            }

            // Check if the crew member is tired. If they are add Modification TIRED else remove it.
            if (getTiredness() == getMaxTiredness()) {
                addModification(Modifications.TIRED);
            } else if (modifications.contains(Modifications.TIRED)){
                removeModification(Modifications.TIRED);
            }

            // Alter crew member values after all possible modifications have been added.
            alterFood(getFoodDecayRate());
            alterHealth(getHealthRegen());
            alterTiredness(getTirednessRate());
            actionsLeftToday = 2;

            // Run through all the onTick() for each modification.
            for (Modifications modification : getModifications()) {
                modification.getInstance().onTick(CrewMember.this);
            }

            // Last thing to do is check the crew member is still alive.
            if (!isAlive()) {
                SpaceExplorer.eventManager.notifyObservers(Event.CREW_MEMBER_DIED, CrewMember.this);
            }
        }
    }
}
