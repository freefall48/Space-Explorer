package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Zoltan extends CrewMember {

    /**
     * Creates a crew member with CrewType Zoltan and a name. Zoltan's have increased health and
     * health regeneration.
     *
     * @param name Name of the crew member.
     */
    public Zoltan(String name) {
        super(name, CrewType.ZOLTAN, STANDARD_MAX_HEALTH + 50, STANDARD_HEALTH_REGEN + 10,
                STANDARD_SHIP_REPAIR, STANDARD_MAX_TIREDNESS, STANDARD_TIREDNESS_RATE,
                STANDARD_FOOD_LEVEL_DECAY, STANDARD_MAX_FOOD_LEVEL);
    }
}
