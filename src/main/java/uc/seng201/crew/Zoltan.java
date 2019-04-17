package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Zoltan extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     *
     * @param name Name of the crew member.
     */
    public Zoltan(String name) {
        super(name, CrewType.ZOLTAN, 100, 15, 2);
    }
}
