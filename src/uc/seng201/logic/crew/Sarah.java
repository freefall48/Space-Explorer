package uc.seng201.logic.crew;

import uc.seng201.logic.medical.Illnesses;

public class Sarah extends CrewMember {
    public Sarah(String name) {
        super(name, CrewTypes.SARAH);

        this.maxHealth = 100;
        this.health = this.maxHealth;
        this.healthRegenRate = 10;

        this.foodDecayRate = 10;

        this.illnesses.add(Illnesses.BLINDNESS);


    }
}
