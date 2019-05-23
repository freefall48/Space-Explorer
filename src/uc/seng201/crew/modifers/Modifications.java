package uc.seng201.crew.modifers;

/**
 * The possible abilities that crew members can have.
 */
public enum Modifications {
    /**
     * Space Plague causes a crew members health regen to become negative.
     */
    SPACE_PLAGUE("Space Plague", "Prevents health regen and takes damage every day left untreated") {
        @Override
        public IModification getInstance() {
            return new ModSpacePlague();
        }
    },
    /**
     * Increases the crew members health regen.
     */
    HEALING_HANDS("Healing Hands", "Increased health regen") {
        @Override
        public IModification getInstance() {
            return new ModHealingHands();
        }
    },
    /**
     * Increases the crew members ship repair ability.
     */
    MECHANICS_TOOLBOX("Mechanics Toolbox", "Increased ship repairing ability") {
        @Override
        public IModification getInstance() {
            return new ModMechanicsToolbox();
        }
    },
    /**
     * Increases the number of items that space traders will sell if
     * a crew member has this modifier.
     */
    FRIENDLY("Friendly", "Increased the amount of items sold by space traders.") {
        @Override
        public IModification getInstance() {
            return new ModFriendly();
        }
    },
    /**
     * Decrease crew member health regen and increases their tiredness rate.
     */
    HUNGRY("Hungry", "Hasn't eaten and slowly starving.") {
        @Override
        public IModification getInstance() {
            return new ModHungry();
        }
    },
    /**
     * Increases the crew members food level decay as well as reducing their
     * available actions each day.
     */
    TIRED("Tired", "Hasn't sleep so has decreased actions and increased food decay rate.") {
        @Override
        public IModification getInstance() {
            return new ModTiredness();
        }
    };

    /**
     * Display name of the modification.
     */
    private String name;
    /**
     * Description of the modification.
     */
    private String description;

    /**
     * Create a modification.
     *
     * @param name        to display for the modification.
     * @param description of the modification.
     */
    Modifications(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns an instance of the modification corresponding to the enum type.
     *
     * @return instance of the modification.
     */
    public abstract IModification getInstance();

    /**
     * Returns the name to display of the modification.
     *
     * @return textual representation of the modification.
     */
    public String toString() {
        return this.name;
    }

    /**
     * Returns the description of the modification.
     *
     * @return modification description.
     */
    public String getDescription() {
        return description;
    }

}
