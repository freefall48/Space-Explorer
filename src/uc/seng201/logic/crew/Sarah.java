package uc.seng201.logic.crew;

import uc.seng201.logic.medical.Illnesses;

/**
 * Specific crew member type.
 */
public class Sarah extends CrewMember {

    /**
     * Creates a crew member with CrewType Sarah and a name.
     * @param name Name of the crew member.
     */
    public Sarah(String name) {
        super(name, CrewType.SARAH);

        this.maxHealth = 100;
        this.health = this.maxHealth;
        this.healthRegenRate = 10;

        this.foodDecayRate = 10;

        this.illnesses.add(Illnesses.BLINDNESS);


    }
}
