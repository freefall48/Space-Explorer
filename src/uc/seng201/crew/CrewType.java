package uc.seng201.crew;

/**
 * The types of Crew Member available to select from.
 */
public enum CrewType {
    /**
     * Has reduced tiredness rate and a higher maximum tiredness.
     */
    CRYSTAL("Crystal") {
        @Override
        public CrewMember getInstance(String name) {
            return new Crystal(name);
        }
    },
    /**
     * Engi regenerate health at a greater rate, become tired
     * slower as well as loose food slower.
     */
    ENGI("Engi") {
        @Override
        public CrewMember getInstance(String name) {
            return new Engi(name);
        }
    },
    /**
     * Humans increase the number of items that the space traders will sell.
     */
    HUMAN("Human") {
        @Override
        public CrewMember getInstance(String name) {
            return new Human(name);
        }
    },
    /**
     * Slugs have decreased food decay rate and a larger maximum food level.
     */
    SLUG("Slug") {
        @Override
        public CrewMember getInstance(String name) {
            return new Slug(name);
        }
    },
    /**
     * Lanius have increased ship repairing.
     */
    LANIUS("Lanius") {
        @Override
        public CrewMember getInstance(String name) {
            return new Lanius(name);
        }
    },
    /**
     * Zoltan's have increased health and health regeneration.
     */
    ZOLTAN("Zoltan") {
        @Override
        public CrewMember getInstance(String name) {
            return new Zoltan(name);
        }
    };

    /**
     * Name to be displayed when printing the enum value.
     */
    private String name;

    /**
     * Type of crew member.
     *
     * @param name to be displayed.
     */
    CrewType(String name) {
        this.name = name;
    }

    /**
     * Returns an instance of a crew member with a type that corresponds to the
     * enum value.
     *
     * @param name of the crew member.
     *
     * @return crew member with a name and the type of the enum.
     */
    public abstract CrewMember getInstance(String name);

    /**
     * Returns the name that should be displayed for the enum.
     *
     * @return name of the enum.
     */
    @Override
    public String toString() {
        return this.name;
    }

}
