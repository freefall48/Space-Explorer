package uc.seng201.crew;

/**
 * Specific crew member type.
 */
class Engi extends CrewMember {

    /**
     * Engi has increased health regen.
     */
    private static final int INCREASED_HEALTH_REGEN = STANDARD_HEALTH_REGEN + 5;
    /**
     * Engi has a decreased tiredness rate.
     */
    private static final int DECREASED_TIREDNESS_RATE = STANDARD_TIREDNESS_RATE - 5;
    /**
     * Engi has a decreased food decay rate.
     */
    private static final int DECREASED_FOOD_DECAY_RATE = STANDARD_FOOD_LEVEL_DECAY - 5;

    /**
     * Creates a crew member with a given name and CrewType of Engi.
     * Engi regenerate health at a greater rate, become tired
     * slower as well as loose food slower.
     *
     * @param name Name of the crew member.
     */
    Engi(final String name) {
        super(name, CrewType.ENGI,
                STANDARD_MAX_HEALTH,
                INCREASED_HEALTH_REGEN,
                STANDARD_SHIP_REPAIR,
                STANDARD_MAX_TIREDNESS,
                DECREASED_TIREDNESS_RATE,
                DECREASED_FOOD_DECAY_RATE,
                STANDARD_MAX_FOOD_LEVEL);
    }
}
