package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Slug extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     *
     * @param name Name of the crew member.
     */
    public Slug(String name) {
        super(name, CrewType.SLUG, 100, 15, 2,100);
    }
}
