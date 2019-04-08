package uc.seng201.crew;

public class Zoltan extends CrewMember {

    public Zoltan(String name) {
        super(name, CrewTypes.ZOLTAN);

//  Set the default stats for this crew member
        setHealth(100);
        setHealthRegenRate(10);
        setTiredness(0);
        setTirednessRate(10);
        setFoodLevel(100);
        setFoodDecayRate(10);
    }
}
