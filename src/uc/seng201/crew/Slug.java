package uc.seng201.crew;

public class Slug extends BaseCrewMember {

    public Slug(String name) {
        super(name, CrewTypes.SLUG);

//  Set the default stats for this crew member
        setHealth(1);
        setHealthRegenRate(0);
        setTiredness(0);
        setTirednessRate(0);
        setHunger(1);
        setHungerDecayRate(0);
    }
}
