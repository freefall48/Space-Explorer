package uc.seng201.crew;

public class Crystal extends CrewMember {

    public Crystal(String name) {
        super(name, CrewTypes.CRYSTAL);

//  Set the default stats for this crew member
        setHealth(100);
        setHealthRegenRate(10);
        setTiredness(0);
        setTirednessRate(20);
        setFoodLevel(100);
        setFoodDecayRate(5);
    }
}
