package uc.seng201.crew;

/*
* Base class that all crew members extend.
*/
public class BaseCrewMember implements CrewMember {

    private String name;
    private CrewTypes crewType;

    private int health;
    private int healthRegenRate;

    private int hunger;
    private int hungerDecayRate;

    private int tiredness;
    private int tirednessRate;

    public BaseCrewMember(String name, CrewTypes crewType) {
        this.name = name;
        this.crewType = crewType;
    }

    public String getName() {
        return name;
    }

    public CrewTypes getCrewType() {
        return crewType;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthRegenRate() {
        return healthRegenRate;
    }

    public void setHealthRegenRate(int healthRegenRate) {
        this.healthRegenRate = healthRegenRate;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHungerDecayRate() {
        return hungerDecayRate;
    }

    public void setHungerDecayRate(int hungerDecayRate) {
        this.hungerDecayRate = hungerDecayRate;
    }

    public int getTiredness() {
        return tiredness;
    }

    public void setTiredness(int tiredness) {
        this.tiredness = tiredness;
    }

    public int getTirednessRate() {
        return tirednessRate;
    }

    public void setTirednessRate(int tirednessRate) {
        this.tirednessRate = tirednessRate;
    }

    @Override
    public void feed() {

    }
}
