package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Lanius extends CrewMember {

    /**
     * Lanius have increased ship repair ability.
     */
    private static final int INCREASED_SHIP_REPAIR = STANDARD_SHIP_REPAIR + 20;
    /**
     * Creates a crew member with CrewType Lanius and a name.
     * Lanius have increased ship repairing.
     *
     * @param name Name of the crew member.
     */
    public Lanius(final String name) {
        super(name, CrewType.LANIUS,
                STANDARD_MAX_HEALTH,
                STANDARD_HEALTH_REGEN,
                INCREASED_SHIP_REPAIR,
                STANDARD_MAX_TIREDNESS,
                STANDARD_TIREDNESS_RATE,
                STANDARD_FOOD_LEVEL_DECAY,
                STANDARD_MAX_FOOD_LEVEL);
    }
}
