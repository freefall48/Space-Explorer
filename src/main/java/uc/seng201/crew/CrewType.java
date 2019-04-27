package uc.seng201.crew;

/*
 * The types of Crew Member available to select from.
 */
public enum CrewType {
    CRYSTAL("Crystal"),
    ENGI("Engi"),
    HUMAN("Human"),
    SLUG("Slug"),
    LANIUS("Lanius"),
    ZOLTAN("Zoltan");

    private String name;

    CrewType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
