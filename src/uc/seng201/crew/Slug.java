package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Slug extends CrewMember {

    /**
     * Slugs have an increased max food level.
     */
    private static final int INCREASED_MAX_FOOD_LEVEL = STANDARD_MAX_FOOD_LEVEL + 50;
    /**
     * Slugs have a decreased food level decay rate.
     */
    private static final int DECREASED_FOOD_DECAY_RATE = STANDARD_FOOD_LEVEL_DECAY - 10;
    /**
     * Creates a crew member with CrewType Slug and a name. Slugs have decreased food decay rate and a larger
     * maximum food level.
     *
     * @param name Name of the crew member.
     */
    public Slug(final String name) {
        super(name, CrewType.SLUG,
                STANDARD_MAX_HEALTH,
                STANDARD_HEALTH_REGEN,
                STANDARD_SHIP_REPAIR,
                STANDARD_MAX_TIREDNESS,
                STANDARD_TIREDNESS_RATE,
                DECREASED_FOOD_DECAY_RATE,
                INCREASED_MAX_FOOD_LEVEL);
    }
}
