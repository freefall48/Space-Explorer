package uc.seng201.crew;

import uc.seng201.crew.modifers.Modifications;

/**
 * Specific crew member type.
 */
public class Human extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name.
     *
     * @param name Name of the crew member.
     */
    public Human(String name) {

        super(name, CrewType.HUMAN);
        addModification(Modifications.FRIENDLY);
    }
}
