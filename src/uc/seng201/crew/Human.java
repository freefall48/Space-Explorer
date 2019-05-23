package uc.seng201.crew;

import uc.seng201.crew.modifers.Modifications;

/**
 * Specific crew member type.
 */
class Human extends CrewMember {

    /**
     * Creates a crew member with CrewType Human and a name. Humans increase the number of items
     * that the space traders will sell.
     *
     * @param name Name of the crew member.
     */
    Human(final String name) {

        super(name, CrewType.HUMAN);
        addModification(Modifications.FRIENDLY);
    }
}
