package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Crystal extends CrewMember {
    /**
     * Crystals have an increased maximum tiredness.
     */
    private static final int INCREASED_MAX_TIREDNESS = STANDARD_MAX_TIREDNESS + 20;
    /**
     * Crystals have a decreased tiredness rate.
     */
    private static final int DECREASE_TIREDNESS_RATE = STANDARD_TIREDNESS_RATE - 10;
    /**
     * Creates a crew member with a given name and of CrewType Crystal.
     * A crystal has an increased max tiredness as becomes tired
     * at a slower rate.
     *
     * @param name Name of the crew member.
     */
    public Crystal(final String name) {
        super(name, CrewType.CRYSTAL,
                STANDARD_MAX_HEALTH,
                STANDARD_HEALTH_REGEN,
                STANDARD_SHIP_REPAIR,
                INCREASED_MAX_TIREDNESS,
                DECREASE_TIREDNESS_RATE,
                STANDARD_FOOD_LEVEL_DECAY,
                STANDARD_MAX_FOOD_LEVEL);
    }
}
