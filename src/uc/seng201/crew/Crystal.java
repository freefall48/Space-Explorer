package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Crystal extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     *
     * @param name Name of the crew member.
     */
    public Crystal(String name) {
        super(name, CrewType.CRYSTAL);
    }
}
