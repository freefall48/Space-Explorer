package uc.seng201.crew.modifers;

/**
 * The possible abilities that crew members can have.
 */
public enum Abilities {
    HEALING_HANDS("Healing Hands"),
    MECHANICS_TOOLBOX("Mechanics Toolbox"),
    TWELVE_PARSECS("Twelve Parsecs");

    private String name;

    Abilities(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

}
