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

    /**
     * Constructor that creates a spaceship object with a given name and the
     * number of missing parts.
     *
     * @param shipName      The name of the ship.
     * @param missingParts  The number of missing parts to find.
     */
    public SpaceShip(String shipName, int missingParts) {
        this.shipName = shipName;
        this.shipCrew = new ArrayList<>();
        this.missingParts = missingParts;
    }

    /**
     * Add crew members to the the spaceship.
     *
     * @param crewMembers CrewMember to add the the spaceship.
     */
    public void addCrewMember(CrewMember... crewMembers) {
        for (CrewMember crewMember : crewMembers) {
            shipCrew.add(crewMember);
        }
    }

    /**
     * Removes a crew member from the spaceship.
     *
     * @param crewMember CrewMember to remove from the spaceship.
     */
    public void removeCrewMember(CrewMember crewMember) {
        shipCrew.remove(crewMember);
    }

    /**
     * Gets the spaceships name.
     *
     * @return Name of the spaceship.
     */
    public String getShipName() {
        return shipName;
    }

    /**
     * Gets all of the current crew members of this spaceship.
     * @return List of all crew members.
     */
    public List<CrewMember> getShipCrew() {
        return shipCrew;
    }

    /**
     * Gets the number of still missing parts of the ship.
     *
     * @return Number of missing parts of the ship.
     */
    public int getMissingParts() {
        return missingParts;
    }


}
