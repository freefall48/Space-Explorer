package uc.seng201.logic;

import uc.seng201.logic.crew.CrewMember;

import java.util.ArrayList;
import java.util.List;

public class SpaceShip {

    private String shipName;
    private List<CrewMember> shipCrew;
    private int missingParts;

    private final int maxShieldCount = 4;
    private int shieldCount = 1;

    private final int maxShipHealth = 50;
    private int shipHealth = maxShipHealth;

    public SpaceShip(String shipName, int missingParts) {
        this.shipName = shipName;
        this.shipCrew = new ArrayList<>();
        this.missingParts = missingParts;
    }

    public void addCrewMember(CrewMember... crewMembers) {
        for (CrewMember crewMember : crewMembers) {
            shipCrew.add(crewMember);
        }
    }

    public void removeCrewMember(CrewMember crewMember) {
        shipCrew.remove(crewMember);
    }

    public String getShipName() {
        return shipName;
    }

    public List<CrewMember> getShipCrew() {
        return shipCrew;
    }

    public int getMissingParts() {
        return missingParts;
    }


}
