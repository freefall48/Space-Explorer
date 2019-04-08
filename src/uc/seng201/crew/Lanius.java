package uc.seng201.crew;

public class Lanius extends CrewMember {

    public Lanius(String name) {
        super(name, CrewTypes.LANIUS);

//  Set the default stats for this crew member
        setHealth(100);
        setHealthRegenRate(30);
        setTiredness(0);
        setTirednessRate(20);
        setFoodLevel(100);
        setFoodDecayRate(15);
    }
}
