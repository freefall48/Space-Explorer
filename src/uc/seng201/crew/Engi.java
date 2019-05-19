package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Engi extends CrewMember {

    /**
     * Creates a crew member with a given name and CrewType of Engi. Engi regenerate health at
     * a greater rate, become tired slower as well as loose food slower.
     *
     * @param name Name of the crew member.
     */
    public Engi(String name) {
        super(name, CrewType.ENGI, STANDARD_MAX_HEALTH, STANDARD_HEALTH_REGEN + 5,
                STANDARD_SHIP_REPAIR, STANDARD_MAX_TIREDNESS, STANDARD_TIREDNESS_RATE - 5,
                STANDARD_FOOD_LEVEL_DECAY + 5, STANDARD_MAX_FOOD_LEVEL);
    }
}
