package uc.seng201.crew;

public class Human extends CrewMember {

    public Human(String name) {
        super(name, CrewTypes.HUMAN);

//  Set the default stats for this crew member
        setHealth(100);
        setHealthRegenRate(5);
        setTiredness(0);
        setTirednessRate(10);
        setFoodLevel(100);
        setFoodDecayRate(20);
    }
}
