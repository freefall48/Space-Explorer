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

    /**
     * Standard starting value for crew health.
     */
    static final int STANDARD_MAX_HEALTH = 100;
    /**
     * Standard health regeneration rate.
     */
    static final int STANDARD_HEALTH_REGEN = 20;
    /**
     * Standard ship repair.
     */
    static final int STANDARD_SHIP_REPAIR = 30;
    /**
     * Standard maximum tiredness.
     */
    static final int STANDARD_MAX_TIREDNESS = 100;
    /**
     * Standard tiredness rate.
     */
    static final int STANDARD_TIREDNESS_RATE = 20;
    /**
     * Standard maximum food level.
     */
    static final int STANDARD_MAX_FOOD_LEVEL = 100;
    /**
     * Standard food decay rate per day.
     */
    static final int STANDARD_FOOD_LEVEL_DECAY = -20;

    /**
     * Crew member name.
     */
    private String name;
    /**
     * Type of the crew member.
     */
    private CrewType crewType;
    /**
     * Maximum health of this crew member.
     */
    private int maxHealth;
    /**
     * Standard health regeneration rate of this crew member.
     */
    private int baseHealthRegen;
    /**
     * Repair ability of the crew member.
     */
    private int repairAmount;
    /**
     * Maximum tiredness of the crew member.
     */
    private int maxTiredness;
    /**
     * The current tiredness rate of the crew member.
     */
    private int currentTirednessRate;
    /**
     * The standard tiredness rate of the crew member.
     */
    private int baseTirednessRate;
    /**
     * The current food decay rate of the crew member.
     */
    private int currentFoodDecayRate;
    /**
     * The standard food decay rate of the crew member.
     */
    private int baseFoodDecayRate;
    /**
     * The current health of the crew member.
     */
    private int health;
    /**
     * The current health regeneration rate of the crew member.
     */
    private int currentHealthRegen;
    /**
     * The current tiredness of the crew member.
     */
    private int tiredness;
    /**
     * The actions the crew member has remaining for the current day.
     */
    private int actionsLeftToday;
    /**
     * Contains all the modifiers that apply to the crew member.
     */
    private Set<Modifications> modifications;
    /**
     * The current food level of the crew member.
     */
    private int foodLevel;
    /**
     * The maximum food level of the crew member.
     */
    private int maxFoodLevel;


    /**
     * Adds the required observers for the crew member class.
     */
    private CrewMember() {
        GameEnvironment.EVENT_MANAGER.addObserver(Event.START_DAY, new NextDay());
    }

    /**
     * Creates a crew member of a given name and type with all other stats set to default.
     *
     * @param name of crew member.
     * @param crewType type of crew member.
     */
    public CrewMember(final String name, final CrewType crewType) {
        this(name, crewType, STANDARD_MAX_HEALTH, STANDARD_HEALTH_REGEN, STANDARD_SHIP_REPAIR, STANDARD_MAX_TIREDNESS,
                STANDARD_TIREDNESS_RATE, STANDARD_FOOD_LEVEL_DECAY, STANDARD_MAX_FOOD_LEVEL);
    }

    /**
     * Creates a crew member where all stats can be customised.
     *
     * @param name of crew member.
     * @param crewType type of crew member.
     * @param maxHealth maximum health of the crew member.
     * @param healthRegen health regeneration rate of crew member.
     * @param repairAmount repair ability of the crew member.
     * @param maxTiredness maximum tiredness of the crew member.
     * @param tirednessRate tiredness rate of the crew member.
     * @param foodDecayRate food decay rate of the crew member.
     * @param maxFoodLevel maximum food level of the crew member.
     */
    public CrewMember(final String name, final CrewType crewType, final int maxHealth, final int healthRegen,
                      final int repairAmount, final int maxTiredness, final int tirednessRate,
                      final int foodDecayRate, final int maxFoodLevel) {
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

    /**
     * Returns the name and type of the crew member.
     *
     * @return name and type.
     */
    @Override
    public String toString() {
        return String.format("%s - %s", name, crewType);
    }

    /**
     * Compares if two crew members are the same.
     *
     * @param obj crew member to compare to.
     * @return true if they are the same crew member.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CrewMember) {
            CrewMember crewMember = (CrewMember) obj;
            return crewMember.getName().equals(name) && crewMember.getCrewType().equals(crewType);
        }
        return false;
    }

    /**
     * Computes the hashcode of this crew member. Required for using a hash-set.
     *
     * @return hashcode.
     */
    @Override
    public int hashCode() {
        return (name + crewType).hashCode();
    }

    /**
     * Provides a textual description of the crew member.
     *
     * @return crew member description.
     */
    public String description() {
        return String.format("%s the %s has %d actions. ",
                this.name, this.crewType, this.actionsLeftToday) +
                String.format("[%d/%d %d/day] HP. [%d/%d %d/day] Food. [%d/%d %d/day] Tiredness. ",
                        this.health, this.maxHealth, this.currentHealthRegen, this.foodLevel,
                        this.maxFoodLevel, this.currentFoodDecayRate, this.tiredness, this.maxTiredness, this.currentTirednessRate);
    }

    /**
     * Returns the current tiredness rate of the crew member.
     *
     * @return tiredness rate.
     */
    public int getTirednessRate() {
        return currentTirednessRate;
    }

    /**
     * Returns the maximum food level of this crew member.
     *
     * @return maximum food level.
     */
    public int getMaxFoodLevel() {
        return maxFoodLevel;
    }

    /**
     * Returns the current food decay rate of the crew member.
     *
     * @return food decay rate.
     */
    public int getFoodDecayRate() {
        return currentFoodDecayRate;
    }

    /**
     * Returns the name of the crew member.
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the maximum health of this crew member.
     *
     * @return maximum health.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Returns the repair ability of the crew member.
     *
     * @return repair ability.
     */
    public int getRepairAmount() {
        return repairAmount;
    }

    /**
     * Returns the maximum tiredness value for this crew member.
     *
     * @return maximum tiredness.
     */
    public int getMaxTiredness() {
        return maxTiredness;
    }

    /**
     * Returns the crew member type.
     *
     * @return crew type.
     */
    public CrewType getCrewType() {
        return crewType;
    }

    /**
     * Returns the current health of the crew member.
     *
     * @return current health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the current health regen rate of the crew member.
     *
     * @return current health regen rate.
     */
    public int getHealthRegen() {
        return currentHealthRegen;
    }

    /**
     * Returns the current tiredness of the crew member.
     *
     * @return current tiredness.
     */
    public int getTiredness() {
        return tiredness;
    }

    /**
     * Returns the number of actions the crew member has left for the current day.
     *
     * @return number of actions remaining.
     */
    public int getActionsLeftToday() {
        return actionsLeftToday;
    }

    /**
     * Returns an unmodifiable view to the modifications of the crew member.
     * The backing collection cannot be altered directly via this reference but
     * it does reflect any changes made to the backing collection.
     *
     * @return crew member modifications.
     */
    public Collection<Modifications> getModifications() {
        return Collections.unmodifiableSet(modifications);
    }

    /**
     * Returns the current food level of the crew member.
     *
     * @return  current food level.
     */
    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Alters the crew members repair ability. Makes sure it is non-negative.
     *
     * @param value to alter the repair ability by.
     */
    public void alterRepairAmount(final int value) {
        int newRepairValue = repairAmount + value;
        if (newRepairValue < 0) {
            newRepairValue = 0;
        }

        repairAmount = newRepairValue;
    }


    /**
     * Restores the crew members health regen to their base stat.
     */
    public void restoreHealthRegen() {
        currentHealthRegen = baseHealthRegen;
    }

    /**
     * Alters the number of actions the crew member has remaining for the current day. Makes
     * sure the new number of actions is non-negative.
     *
     * @param value to alter the actions left by.
     */
    public void alterActionsLeft(final int value) {
        int newActionsLeft = actionsLeftToday + value;
        if (newActionsLeft < 0) {
            newActionsLeft = 0;
        }
        actionsLeftToday = newActionsLeft;
    }

    /**
     * Alters the health regen of the crew member.
     *
     * @param value to alter the health regen by.
     */
    public void alterHealthRegen(final int value) {
        currentHealthRegen += value;
    }

    /**
     * Alters the crew members food level. If the new food level reaches 0 then the
     * hungry modifier is added to the crew member. If the hunger is increased
     * from zero then the modifier is then removed.
     *
     * @param value to alter the food level by.
     */
    public void alterFood(final int value) {
        int newLevel = this.foodLevel + value;
        if (newLevel <= 0) {
            newLevel = 0;
            addModification(Modifications.HUNGRY);
        } else {
            removeModification(Modifications.HUNGRY);
        }
        if (newLevel > maxFoodLevel) {
            newLevel = maxFoodLevel;
        }
        foodLevel = newLevel;
    }

    /**
     * Restores the crew members food decay rate to their base rate.
     */
    public void restoreFoodDecayRate() {
        currentFoodDecayRate = baseFoodDecayRate;
    }

    /**
     * Alters the crew members food decay rate. Makes sure the bew
     * rate is non-negative.
     *
     * @param value to alter food decay rate by.
     */
    public void alterFoodDecayRate(final int value) {
        int newFoodDecayRate = currentFoodDecayRate + value;
        if (newFoodDecayRate < 0) {
            newFoodDecayRate = 0;
        }
        currentFoodDecayRate = newFoodDecayRate;
    }

    /**
     * Restores the crew members tiredness rate to the base rate for
     * this crew member.
     */
    public void restoreTirednessRate() {
        currentTirednessRate = baseTirednessRate;
    }

    /**
     * Alters the crew members tiredness rate. Makes sure that the rate
     * is non-negative.
     *
     * @param value to alter the tiredness rate by.
     */
    public void alterTirednessRate(final int value) {
        int newTirednessRate = currentTirednessRate + value;
        if (newTirednessRate < 0) {
            newTirednessRate = 0;
        }
        currentTirednessRate = newTirednessRate;
    }

    /**
     * Alters the current health of the crew member. Makes sure the maximum
     * health is not exceeded. If the crew members new health would be equal to
     * or less than 0 then the event manager is notified of the Crew Member Died
     * event.
     *
     * @param value to alter the current health by.
     */
    public void alterHealth(final int value) {

        int newHealth = this.health + value;
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        } else if (newHealth <= 0) {
            newHealth = 0;
            GameEnvironment.EVENT_MANAGER.notifyObservers(Event.CREW_MEMBER_DIED, this);
        }
        this.health = newHealth;
    }

    /**
     * Restores crew members health regen to their base rate.
     */
    public void restoreTiredness() {
        tiredness = 0;
    }

    /**
     * Alters the tiredness of the crew member by a given value. If the new
     * tiredness is equal or exceeds the maximum tiredness then the
     * TIRED modifier is added. If the tiredness is below maximum tiredness then
     * the TIRED modifier is removed.
     *
     * @param value to alter the current tiredness by.
     */
    public void alterTiredness(final int value) {
        int newValue = this.tiredness + value;

        // Make sure the maximum tiredness is not exceeded and apply or remove Tired modifier.
        if (newValue >= this.maxTiredness) {
            newValue = this.maxTiredness;
            addModification(Modifications.TIRED);
        } else  {
            removeModification(Modifications.TIRED);
        }
        // Make sure the value is non-negative.
        if (newValue < 0) {
            newValue = 0;
        }
        this.tiredness = newValue;
    }

    /**
     * Applies an illness to the crew member. The Illnesses onAdd() method
     * is called if the crew member did not already contain the modification.
     *
     * @param modification Modification to add to crew member
     */
    public void addModification(final Modifications modification) {
        // If true then it was added else it was already present.
        if (modifications.add(modification)) {
            modification.getInstance().onAdd(this);
        }
    }

    /**
     * Removes an illness from a crew member. The Illnesses onRemove()
     * method is called if the crew member did not already contain the modification.
     *
     * @param modification The Illness to remove from the crew member.
     */
    public void removeModification(final Modifications modification) {
        // If true then the crew member had the modification else it was not present.
        if (modifications.remove(modification)) {
            modification.getInstance().onRemove(this);
        }
    }

    /**
     * Returns if the crew member can perform actions that require action points.
     *
     * @return true if the crew member can perform actions.
     */
    public boolean canPerformActions() {
        return this.actionsLeftToday > 0;
    }

    /**
     * Reduces the number of action points remaining for the day.
     *
     * @throws CrewMemberException if the crew member has no action points remaining when called.
     */
    public void performAction() {
        if (this.actionsLeftToday > 0) {
            this.actionsLeftToday -= 1;
        } else {
            throw new CrewMemberException();
        }
    }

    /**
     * Returns if the current crew member is alive.
     * @return true if the crew member is alive.
     */
    boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Handler for the next day event for crew members. When the Next day event is called. Alters
     * the crew members stats. If the crew member dies, the event manager is notified and this
     * observer is removed from the list of observers for the Start day event.
     */
    final class NextDay implements Observer {

        @Override
        public void onEvent(final Object... args) {
            actionsLeftToday = 2;
            alterFood(getFoodDecayRate());
            alterTiredness(getTirednessRate());
            alterHealth(getHealthRegen());

            // If the crew member dies no need form them to listen to this event.
            if (!isAlive()) {
                GameEnvironment.EVENT_MANAGER.removeObserver(Event.START_DAY, this);
                return;
            }

            // Run through all the onTick() for each modification.
            for (Modifications modification : getModifications()) {
                modification.getInstance().onTick(CrewMember.this);
            }



        }
    }
}
