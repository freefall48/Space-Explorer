package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.errors.InsufficientBalance;
import uc.seng201.items.ItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceShip {

    private String shipName;
    private List<CrewMember> shipCrew;
    private List<ItemType> shipItems;
    private int missingParts;

    private int spaceBucks;

    private final int maxShieldCount = 4;
    private int shieldCount = 2;

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
        this.shipItems = new ArrayList<>();
        this.missingParts = missingParts;
    }

    public String toString() {
        return String.format("Ship '%s' has %d shields and missing %d parts.\n" +
                "The crew consists of:\n%s",
                shipName, shieldCount, missingParts, Helpers.listToString(shipCrew, true));
    }

    /**
     * Add crew members to the the spaceship.
     *
     * @param crewMembers CrewMembers to add the the spaceship.
     */
    public void add(CrewMember... crewMembers) {
        shipCrew.addAll(Arrays.asList(crewMembers));
    }

    public boolean contains(ItemType item) {
        return shipItems.contains(item);
    }

    public boolean contains(CrewMember crewMember) {
        return shipCrew.contains(crewMember);
    }

    public void remove(ItemType itemType) {
        this.shipItems.remove(itemType);
    }

    public void add(ItemType itemType) {
        this.shipItems.add(itemType);
    }

    /**
     * Removes a crew member from the spaceship.
     *
     * @param crewMember CrewMember to remove from the spaceship.
     */
    public void remove(CrewMember crewMember) {
        this.shipCrew.remove(crewMember);
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

    public List<ItemType> getShipItems() { return  shipItems; }

    /**
     * Gets the number of still missing parts of the ship.
     *
     * @return Number of missing parts of the ship.
     */
    public int getMissingParts() {
        return missingParts;
    }

    public void partFound() {
        this.missingParts -= 1;
    }

    public void alterShield(int value) {
        int newShieldValue = this.shieldCount + value;
        if (newShieldValue > this.maxShieldCount) {
            newShieldValue = this.maxShieldCount;
        } else if(newShieldValue < 0) {
            newShieldValue = 0;
        }
        this.shieldCount = newShieldValue;
    }

    public void alterSpaceBucks(int value) throws InsufficientBalance {
        int newBalance = this.spaceBucks + value;
        if (newBalance < 0) {
            throw new InsufficientBalance();
        }
        this.spaceBucks = newBalance;
    }

    public void turn() {
        for (CrewMember crewMember : shipCrew) {
            crewMember.updateStats();
        }
    }


}
