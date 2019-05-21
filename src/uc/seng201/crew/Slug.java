package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Slug extends CrewMember {

    /**
     * Creates a crew member with CrewType Slug and a name. Slugs have decreased food decay rate and a larger
     * maximum food level.
     *
     * @param name Name of the crew member.
     */
    public Slug(String name) {
        super(name, CrewType.SLUG, CrewMember.STANDARD_MAX_HEALTH, CrewMember.STANDARD_HEALTH_REGEN,
                CrewMember.STANDARD_SHIP_REPAIR,CrewMember.STANDARD_MAX_TIREDNESS,CrewMember.STANDARD_TIREDNESS_RATE,
                CrewMember.STANDARD_FOOD_LEVEL_DECAY + 10,CrewMember.STANDARD_MAX_FOOD_LEVEL + 50);
    }
}
