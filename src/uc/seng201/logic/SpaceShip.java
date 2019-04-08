package uc.seng201.logic;

import uc.seng201.logic.crew.CrewMember;

public class SpaceShip {

    private String shipName;
    private CrewMember[] shipCrew;

    public SpaceShip(String shipName, CrewMember[] shipCrew) {
        this.shipName = shipName;
        this.shipCrew = shipCrew;
    }

    public String getShipName() {
        return shipName;
    }

    public CrewMember[] getShipCrew() {
        return shipCrew;
    }
}
