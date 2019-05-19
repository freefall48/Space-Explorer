package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Crystal extends CrewMember {

    /**
     * Creates a crew member with a given name and of CrewType Crystal. A crystal has an increased
     * max tiredness as becomes tired at a slower rate.
     *
     * @param name Name of the crew member.
     */
    public Crystal(String name) {
        super(name, CrewType.CRYSTAL, STANDARD_MAX_HEALTH, STANDARD_HEALTH_REGEN,
                STANDARD_SHIP_REPAIR, STANDARD_MAX_TIREDNESS + 20, STANDARD_TIREDNESS_RATE - 10,
                STANDARD_FOOD_LEVEL_DECAY, STANDARD_MAX_FOOD_LEVEL);
    }
}
