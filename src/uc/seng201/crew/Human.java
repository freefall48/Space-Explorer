package uc.seng201.crew;

public class Human extends BaseCrewMember{

    public Human(String name) {
        super(name, CrewTypes.ENGI);

//  Set the default stats for this crew member
        setHealth(1);
        setHealthRegenRate(0);
        setTiredness(0);
        setTirednessRate(0);
        setHunger(1);
        setHungerDecayRate(0);
        feed();
    }
}
