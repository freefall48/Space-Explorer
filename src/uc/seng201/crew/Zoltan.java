package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Zoltan extends CrewMember {

    /**
     * Zoltans have an increased max health.
     */
    private static final int INCREASED_MAX_HEALTH = STANDARD_MAX_HEALTH + 50;
    /**
     * Zoltans have an increased health regen.
     */
    private static final int INCREASED_HEALTH_REGEN = STANDARD_HEALTH_REGEN + 10;
    /**
     * Creates a crew member with CrewType Zoltan and a name.
     * Zoltan's have increased health and health regeneration.
     *
     * @param name Name of the crew member.
     */
    public Zoltan(final String name) {
        super(name, CrewType.ZOLTAN,
                INCREASED_MAX_HEALTH,
                INCREASED_HEALTH_REGEN,
                STANDARD_SHIP_REPAIR,
                STANDARD_MAX_TIREDNESS,
                STANDARD_TIREDNESS_RATE,
                STANDARD_FOOD_LEVEL_DECAY,
                STANDARD_MAX_FOOD_LEVEL);
    }
}
