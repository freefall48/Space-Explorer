package uc.seng201.crew;

import uc.seng201.medical.Illnesses;

public class Sarah extends CrewMember {
    public Sarah(String name) {
        super(name, CrewTypes.SARAH);
        addIllness(Illnesses.BLINDNESS);

//  Set the default stats for this crew member
        setHealth(1000000000);
        setHealthRegenRate(10000000);
        setTiredness(0);
        setTirednessRate(0);
        setFoodLevel(10000000);
        setFoodDecayRate(0);
    }
}
