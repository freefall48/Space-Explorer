package uc.seng201.crew.modifers;

/**
 * The possible abilities that crew members can have.
 */
public enum Modifications {
    SPACE_PLAGUE("Space Plague", "Prevents health regen and takes damage every day left untreated") {
        @Override
        public IModification getInstance() {
            return new ModSpacePlague();
        }
    },
    HEALING_HANDS("Healing Hands", "Increased health regen"){
        @Override
        public IModification getInstance() {
            return new ModHealingHands();
        }
    },
    MECHANICS_TOOLBOX("Mechanics Toolbox", "Increased ship repairing ability"){
        @Override
        public IModification getInstance() {
            return new ModMechanicsToolbox();
        }
    },
    FRIENDLY("Friendly", "Increased the amount of items sold by space traders."){
        @Override
        public IModification getInstance() {
            return new ModFriendly();
        }
    };

    private String name;
    private String description;

    Modifications(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public abstract IModification getInstance();

    public String toString() {
        return this.name;
    }

    public String getDescription() {return description;}

}
