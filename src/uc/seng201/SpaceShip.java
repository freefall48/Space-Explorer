package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.errors.InsufficientBalance;
import uc.seng201.errors.InvalidGameState;
import uc.seng201.errors.SpaceShipException;
import uc.seng201.items.SpaceItem;

import java.util.*;

public class SpaceShip {

    private String shipName;
    private List<CrewMember> shipCrew;
    private Map<SpaceItem, Integer> shipItems;
    private int missingParts;
    private final int missingPartsAtStart;
    private int spaceBucks = 0;
    private int shieldCount = 2;

    /**
     * Constructor that creates a spaceship object with a given name and the
     * number of missing parts.
     *
     * @param shipName     The name of the ship.
     * @param missingParts The number of missing parts to crewMemberFromName.
     */
    public SpaceShip(String shipName, int missingParts) {
        this.shipName = shipName;
        this.shipCrew = new ArrayList<>();
        this.shipItems = new HashMap<>();
        this.missingParts = this.missingPartsAtStart = missingParts;
    }

//    public String toString() {
//        return String.format("'%s' has %d shields and missing %d parts.\n",
//                this.shipName, this.shieldCount, this.missingParts) +
//                String.format("Crew: \n%s\n", Helpers.listToString(this.shipCrew, true)) +
//                String.format("SpaceItem:\n%s\n", Helpers.listToString(shipItems.values())) +
//                String.format("Money: $%d\n", this.spaceBucks);
//    }

    public boolean add(Collection<CrewMember> crewMembers) {
        return shipCrew.addAll(crewMembers);
    }

    public void add(SpaceItem item) {
        if (shipItems.containsKey(item)) {
            shipItems.put(item, shipItems.get(item) + 1);
        } else {
            shipItems.put(item, 1);
        }
    }

    public CrewMember crewMemberFromName(String crewMemberName) throws NullPointerException {
        for (CrewMember crewMember : this.shipCrew) {
            if (crewMember.getName().toUpperCase().equals(crewMemberName.toUpperCase())) {
                return crewMember;
            }
        }
        throw new NullPointerException("Spaceship does not contain crew member with name " + crewMemberName);
    }

    public boolean contains(SpaceItem item) {
        return this.shipItems.containsKey(item);
    }

    public boolean contains(CrewMember crewMember) {
        return this.shipCrew.contains(crewMember);
    }

    public int getMissingPartsAtStart() {
        return missingPartsAtStart;
    }

    public void remove(SpaceItem item) throws InvalidGameState, NullPointerException {
        if (contains(item)) {
            int itemCount = shipItems.get(item);
            if (itemCount <= 0) {
                throw new InvalidGameState("GameState has ghost item " + item);
            }
            itemCount -= 1;
            if (itemCount == 0) {
                shipItems.remove(item);
            } else {
                shipItems.put(item, itemCount);
            }
        } else {
            throw new NullPointerException("Spaceship does not contain that item " + item);
        }
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
     *
     * @return List of all crew members.
     */
    public List<CrewMember> getShipCrew() {

        return this.shipCrew;

    }


    public Map<SpaceItem, Integer> getShipItems() {
        return shipItems;
    }

    public int getSpaceBucks() {
        return spaceBucks;
    }

    public int getShieldCount() {
        return shieldCount;
    }

    /**
     * Gets the number of still missing parts of the ship.
     *
     * @return Number of missing parts of the ship.
     */
    public int getMissingParts() {
        return missingParts;
    }

    public void partFound() throws InvalidGameState {
        if (missingParts > 0) {
            missingParts -= 1;
        } else {
            throw new InvalidGameState("Cannot reduce parts if no parts are missing");
        }
    }

    public void alterShield(int value) throws SpaceShipException {
        int newShieldValue = this.shieldCount + value;
        if (newShieldValue <= 0) {
            throw new SpaceShipException("No spaceship shields remaining");
        }
        this.shieldCount = newShieldValue;
    }

    public void alterSpaceBucks(int value) throws InsufficientBalance {
        int newBalance = this.spaceBucks + value;
        if (newBalance < 0) {
            throw new SpaceShipException("Cannot remove " + value + " from Spaceship balance");
        }
        this.spaceBucks = newBalance;
    }

    public boolean hasCrewActionsRemaining() {
        for (CrewMember crewMember : this.shipCrew) {
            if (crewMember.getActionsLeftToday() > 0) {
                return true;
            }
        }
        return false;
    }
}
