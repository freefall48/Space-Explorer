package uc.seng201.crew;

/**
 * The possible illnesses that can affect crew members.
 */
public enum Illnesses {
    SPACE_PLAGUE("Space Plague"),
    BLINDNESS("Blindness"),
    PARANOIA("Paranoia"),
    SPACE_PARASITE("Space Parasite");

    private String name;

    Illnesses(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
