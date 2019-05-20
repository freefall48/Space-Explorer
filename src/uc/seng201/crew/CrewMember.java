package uc.seng201.crew;

import uc.seng201.environment.GameEnvironment;
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

    static final int STANDARD_MAX_HEALTH = 100;
    static final int STANDARD_HEALTH_REGEN = 20;
    static final int STANDARD_SHIP_REPAIR = 30;
    static final int STANDARD_MAX_TIREDNESS = 100;
    static final int STANDARD_TIREDNESS_RATE = 20;
    static final int STANDARD_MAX_FOOD_LEVEL = 100;
    static final int STANDARD_FOOD_LEVEL_DECAY = -20;

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
        GameEnvironment.eventManager.addObserver(Event.START_DAY, new NextDay());
    }

    public CrewMember(String name, CrewType crewType) {
        this(name, crewType, STANDARD_MAX_HEALTH, STANDARD_HEALTH_REGEN, STANDARD_SHIP_REPAIR, STANDARD_MAX_TIREDNESS,
                STANDARD_TIREDNESS_RATE, STANDARD_FOOD_LEVEL_DECAY, STANDARD_MAX_FOOD_LEVEL);
    }

    public CrewMember(String name, CrewType crewType, int maxHealth, int healthRegen, int repairAmount,
                      int maxTiredness, int tirednessRate, int foodDecayRate, int maxFoodLevel) {
        this();
        this.name = name;
        this.crewType = crewType;
        this.maxHealth = health = maxHealth;
        this.baseHealthRegen = currentHealthRegen = healthRegen;
        this.repairAmount = repairAmount;
        this.maxTiredness = maxTiredness;
        this.currentTirednessRate = baseTirednessRate = tirednessRate;
        this.currentFoodDecayRate = baseFoodDecayRate = foodDecayRate;
        this.modifications = new HashSet<>();
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

    public void alterRepairAmount(int value) {
        int newRepairValue = repairAmount + value;
        if (newRepairValue < 0) {
            newRepairValue = 0;
        }

        repairAmount = newRepairValue;
    }


    public void restoreHealthRegen() {
        currentHealthRegen = baseHealthRegen;
    }

    public void alterActionsLeft(int value) {
        int newActionsLeft = actionsLeftToday + value;
        if (newActionsLeft < 0) {
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
            GameEnvironment.eventManager.notifyObservers(Event.CREW_MEMBER_DIED, this);
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
            actionsLeftToday = 2;

            // Check if the crew member is hungry
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

            // Run through all the onTick() for each modification.
            for (Modifications modification : getModifications()) {
                modification.getInstance().onTick(CrewMember.this);
            }

            // Alter crew member values after all possible modifications have been added.
            alterFood(getFoodDecayRate());
            alterTiredness(getTirednessRate());
            alterHealth(getHealthRegen());

            // If the crew member dies no need form them to listen to this event.
            if (!isAlive()) {
                GameEnvironment.eventManager.removeObserver(Event.START_DAY, this);
            }
        }
    }
}
