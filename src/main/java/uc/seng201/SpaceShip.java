package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.errors.InsufficientBalance;
import uc.seng201.gui.SpaceExplorerGui;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.Items;

import java.util.ArrayList;
import java.util.List;

public class SpaceShip {

    private final int maxShieldCount = 4;
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
        return String.format("The spaceship '%s' has %d|%d shields and missing %d parts.\n",
                this.shipName, this.shieldCount, this.maxShieldCount, this.missingParts) +
                String.format("The crew consists of: \n%s\n", Helpers.listToString(this.shipCrew, true)) +
                String.format("Items onboard:\n%s\n", Helpers.listToString(this.shipItems)) +
                String.format("Spacebucks balance: $%d\n", this.spaceBucks);
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

        return new ArrayList<>(this.shipCrew);

    }
    public List<CrewMember> getShipCrew(boolean mutable) {
        if (mutable) {
            return this.shipCrew;
        } else {
            return getShipCrew();
        }
    }

    public List<Items> getShipItems() {
        return shipItems;
    }

    public int getMaxShieldCount() {
        return maxShieldCount;
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
        if (newShieldValue > this.maxShieldCount) {
            newShieldValue = this.maxShieldCount;
        } else if (newShieldValue < 0) {
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

    public void startOfDay() {
        this.shipCrew.forEach(CrewMember::updateStats);
        this.shipCrew.forEach(crewMember -> {
            if (crewMember.getTiredness() == crewMember.getMaxTiredness()) {
                crewMember.alterTiredness(0 - crewMember.getMaxTiredness());
                crewMember.performAction();
                SpaceExplorerGui.popup(crewMember.getName() + " was overcome with tiredness and forced to spend" +
                        "an action sleeping.");
            }
        });
    }
}
