package uc.seng201.crew;

public class Engi extends CrewMember {

    public Engi(String name) {
        super(name, CrewTypes.ENGI);

//  Set the default stats for this crew member
        setHealth(1000);
        setHealthRegenRate(100);
        setTiredness(0);
        setTirednessRate(0);
        setFoodLevel(100);
        setFoodDecayRate(0);
    }
}
