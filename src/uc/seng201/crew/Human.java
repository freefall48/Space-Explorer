package uc.seng201.crew;

/**
 * Specific crew member type.
 */
public class Human extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     * @param name Name of the crew member.
     */
    public Human(String name) {
        super(name, CrewType.HUMAN, 100, 15, 2);
        this.addIllness(Illnesses.SPACE_PLAGUE);
    }
}
