package uc.seng201.crew;

public class Slug extends CrewMember {

    public Slug(String name) {
        super(name, CrewTypes.SLUG);

//  Set the default stats for this crew member
        setHealth(100);
        setHealthRegenRate(10);
        setTiredness(0);
        setTirednessRate(40);
        setFoodLevel(100);
        setFoodDecayRate(5);
    }
}
