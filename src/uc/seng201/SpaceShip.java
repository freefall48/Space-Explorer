package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.errors.InsufficientBalance;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.Items;

import java.util.ArrayList;
import java.util.List;

public class SpaceShip {

    private String shipName;
    private List<CrewMember> shipCrew;
    private List<Items> shipItems;
    private int missingParts;
    private int spaceBucks = 0;
    private int shieldCount = 2;

    /**
     * Constructor that creates a spaceship object with a given name and the
     * number of missing parts.
     *
     * @param shipName     The name of the ship.
     * @param missingParts The number of missing parts to findCrewMember.
     */
    public SpaceShip(String shipName, int missingParts) {
        this.shipName = shipName;
        this.shipCrew = new ArrayList<>();
        this.shipItems = new ArrayList<>();
        this.missingParts = missingParts;
    }

    public String toString() {
        return String.format("'%s' has %d shields and missing %d parts.\n",
                this.shipName, this.shieldCount, this.missingParts) +
                String.format("Crew: \n%s\n", Helpers.listToString(this.shipCrew, true)) +
                String.format("Items:\n%s\n", Helpers.listToString(this.shipItems)) +
                String.format("Money: $%d\n", this.spaceBucks);
    }

    /**
     * Add crew members to the the spaceship.
     *
     * @param crewMembers CrewMembers to add the the spaceship.
     */
    public void add(Object... crewMembers) {
        for (Object crewMember : crewMembers) {
                this.shipCrew.add((CrewMember) crewMember);
        }
    }

    public void add(List<CrewMember> crewMembers) {
        this.shipCrew.addAll(crewMembers);
    }

    public void add(Items itemType) {
        this.shipItems.add(itemType);
    }

    public CrewMember findCrewMember(String crewMemberName) {
        for (CrewMember crewMember : this.shipCrew) {
            if (crewMember.getName().toUpperCase().equals(crewMemberName.toUpperCase())) {
                return crewMember;
            }
        }
        return null;
    }

    public boolean contains(Items item) {
        return this.shipItems.contains(item);
    }

    public boolean contains(CrewMember crewMember) {
        return this.shipCrew.contains(crewMember);
    }

    public void remove(Items itemType) {
        this.shipItems.remove(itemType);
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


    public List<Items> getShipItems() {
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

    public void partFound() {
        this.missingParts -= 1;
    }

    public void alterShield(int value) {
        int newShieldValue = this.shieldCount + value;
        if (newShieldValue < 0) {
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


    public void nextDay() {
        shipCrew.forEach(CrewMember::nextDay);
        shipCrew.forEach(crewMember -> {
            if (!crewMember.isAlive()) {
                shipCrew.remove(crewMember);
            }
        });
        if (shipCrew.size() == 0) {
            SpaceExplorer.failedGame("Looks like you have run out of crew...");
        }
        if (shieldCount == 0) {
            SpaceExplorer.failedGame("Looks like you have managed to destroy whats left of " + getShipName());
        }
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
