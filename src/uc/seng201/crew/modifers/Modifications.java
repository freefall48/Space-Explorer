package uc.seng201.crew.modifers;

/**
 * The possible abilities that crew members can have.
 */
public enum Modifications {
    SPACE_PLAGUE("Space Plague") {
        @Override
        public IModification getInstance() {
            return new ModSpacePlague();
        }
    },
    HEALING_HANDS("Healing Hands"){
        @Override
        public IModification getInstance() {
            return new ModHealingHands();
        }
    },
    MECHANICS_TOOLBOX("Mechanics Toolbox"){
        @Override
        public IModification getInstance() {
            return new ModMechanicsToolbox();
        }
    };

    private String name;

    Modifications(final String name) {
        this.name = name;
    }

    public abstract IModification getInstance();

    public String toString() {
        return this.name;
    }

}
