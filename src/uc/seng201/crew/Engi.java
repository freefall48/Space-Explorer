package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Engi extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     *
     * @param name Name of the crew member.
     */
    public Engi(String name) {
        super(name, CrewType.ENGI, 100, 15, 2,100);
    }
}
