package uc.seng201;

import uc.seng201.crew.BaseCrewMember;

public class SpaceShip {

    private String shipName;
    private BaseCrewMember[] shipCrew;

    public SpaceShip(String shipName, BaseCrewMember[] shipCrew) {
        this.shipName = shipName;
        this.shipCrew = shipCrew;
    }

    public String getShipName() {
        return shipName;
    }

    public BaseCrewMember[] getShipCrew() {
        return shipCrew;
    }
}
