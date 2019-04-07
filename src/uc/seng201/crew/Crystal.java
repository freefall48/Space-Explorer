package uc.seng201.crew;

public class Crystal extends BaseCrewMember {

    public Crystal(String name) {
        super(name, CrewTypes.CRYSTAL);

//  Set the default stats for this crew member
        setHealth(1);
        setHealthRegenRate(0);
        setTiredness(0);
        setTirednessRate(0);
        setHunger(1);
        setHungerDecayRate(0);
    }
}
