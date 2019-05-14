package uc.seng201;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.errors.InvalidGameState;
import uc.seng201.errors.SpaceShipException;
import uc.seng201.items.SpaceItem;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.Observer;

import java.util.*;

/**
 * Implementation of a space ship.
 */
public class SpaceShip {

    /**
     * Name of the spaceship.
     */
    private String shipName;
    /**
     * List of crew members on the spaceship.
     */
    private Set<CrewMember> shipCrew;
    /**
     * Collection of the ships items.
     */
    private Map<SpaceItem, Integer> shipItems;
    /**
     * Current number of the missing parts of the ship.
     */
    private int missingParts;
    /**
     * Initial amount of missing parts of the ship when it was created.
     */
    private int originalMissingParts;
    /**
     * Balance in dollars that the spaceship has.
     */
    private int balance;
    /**
     * Number of shields that the spaceship has. If it reaches 0 the ship should die.
     */
    private int shieldCount;
    /**
     * Maximum number of crew members that are allowed on the spaceship. Global to all spaceships.
     */
    public static final int MAXIMUM_CREW_COUNT = 4;
    /**
     * Minimum number of crew members needed for this spaceship.Global to all spaceships.
     */
    public static final int MINIMUM_CREW_COUNT = 2;

    /**
     * Creates a spaceship with a name and missing part count and defaults
     * the remaining variables.
     *
     * @param shipName     The name of the ship.
     * @param missingParts The number of missing parts to crewMemberFromName.
     */
    public SpaceShip(String shipName, int missingParts) {
        this();
        this.shipName = shipName;
        this.missingParts = originalMissingParts = missingParts;
        shipCrew = new HashSet<>();
        shipItems = new HashMap<>();
        balance = 0;
        shieldCount = 2;
    }

    /**
     * Adds the event listeners for this class to the event manager. Also provided
     * to help Gson when creating object instances.
     */
    private SpaceShip() {
        SpaceExplorer.eventManager.addObserver(Event.START_DAY, new NextDayHandler());
        SpaceExplorer.eventManager.addObserver(Event.CREW_MEMBER_DIED, new CrewMemberDiedHandler());
        SpaceExplorer.eventManager.addObserver(Event.BUY_FROM_TRADERS, new BuyFromTradersHandler());
    }

    /**
     * Calculates the number of spaceship parts that should be found based
     * on the number of days the game should run over.
     *
     * @param duration number of days the game will run.
     * @return number of parts that should be found.
     */
    public static int calcPartsToFind(int duration) {
        return (duration * 2/3);
    }

//    public String toString() {
//        return String.format("'%s' has %d shields and missing %d parts.\n",
//                this.shipName, this.shieldCount, this.missingParts) +
//                String.format("Crew: \n%s\n", Helpers.listToString(this.shipCrew, true)) +
//                String.format("SpaceItem:\n%s\n", Helpers.listToString(shipItems.values())) +
//                String.format("Money: $%d\n", this.balance);
//    }

    /**
     * Adds crew members to the spaceship. The new crew size is checked to be
     * within the the maximum and minimum crew size allowed.
     *
     * @param crewMembers collection containing crew members to be added to the ship crew.
     * @return true if all crew members were added to the ship crew.
     */
    public boolean add(Set<CrewMember> crewMembers) {
        int newCrewSize = crewMembers.size() + shipCrew.size();
        if (newCrewSize >= MINIMUM_CREW_COUNT && newCrewSize <= MAXIMUM_CREW_COUNT) {
            return shipCrew.addAll(crewMembers);
        }
        return false;

    }

    /**
     * Adds an item to the ships items. If the ship already contains the
     * item the current quantity of the item is incremented by 1.
     *
     * @param item item to be added to the ships items.
     */
    public void add(SpaceItem item) {
        if (shipItems.containsKey(item)) {
            shipItems.put(item, shipItems.get(item) + 1);
        } else {
            shipItems.put(item, 1);
        }
    }

    /**
     * Returns the first crew member occurrence that has that name, otherwise null.
     *
     * @param crewMemberName name of the crew member to look up
     * @return crew member if present, null if not.
     */
    public CrewMember crewMemberFromNameAndType(String crewMemberName, String type) {
        for (CrewMember crewMember : this.shipCrew) {
            if (crewMember.getName().toUpperCase().equals(crewMemberName.toUpperCase()) &&
            crewMember.getCrewType().equals(CrewType.valueOf(type.toUpperCase()))) {
                return crewMember;
            }
        }
        return null;
    }

    /**
     * Returns true if the spaceships inventory contains the specified space item.
     *
     * @param item element whose presence in the list is to be tested.
     * @return true if the spaceship contains the space item.
     */
    public boolean contains(SpaceItem item) {
        return this.shipItems.containsKey(item);
    }

    /**
     * Returns true if the spaceships crew contains the specified crew member.
     *
     * @param crewMember crew member whose presence in the list is to be tested.
     * @return true if the spaceship contains the element.
     */
    public boolean contains(CrewMember crewMember) {
        return this.shipCrew.contains(crewMember);
    }

    /**
     * Returns the initial number of missing parts when the spaceship was created.
     *
     * @return number of missing parts at spaceship creation.
     */
    public int getOriginalMissingParts() {
        return originalMissingParts;
    }

    /**
     * Removes the space item from the ships inventory and returns it, null if
     * the item was not present.
     *
     * @param item space item that is to be removed.
     * @return the space item that was removed, or null if no item was present.
     *
     * @throws InvalidGameState if the ships inventory contains a 0 or negative count of the item.
     */
    public SpaceItem remove(SpaceItem item) throws InvalidGameState {
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
            return item;
        } else {
            return null;
        }
    }

    /**
     * Removes a crew member from the spaceship.
     *
     * @param crewMember CrewMember to remove from the spaceship.
     * @return true if the crew member was removed.
     */
    public boolean remove(CrewMember crewMember) {
        return shipCrew.remove(crewMember);
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
    public Set<CrewMember> getShipCrew() {

        return Collections.unmodifiableSet(shipCrew);

    }

    /**
     * Returns the Map of space items and their quantities in the ships
     * inventory.
     *
     * @return map of space items and quantities.
     */
    public Map<SpaceItem, Integer> getShipItems() {
        return Collections.unmodifiableMap(shipItems);
    }

    /**
     * Returns the current balance of the spaceship.
     *
     * @return balance of the spaceship.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Returns the current number of shields that the spaceship has.
     *
     * @return number of shields.
     */
    public int getShieldCount() {
        return shieldCount;
    }

    /**
     * Returns the number of parts that are still currently missing from the spaceship.
     *
     * @return number of missing parts.
     */
    public int getMissingParts() {
        return missingParts;
    }

    /**
     * Reduced the number of currently missing spaceship parts by 1.
     *
     * @throws InvalidGameState if the number of parts still missing would be 0 or negative as a part
     * could not have been missing.
     */
    public void partFound() throws InvalidGameState {
        if (missingParts > 0) {
            missingParts -= 1;
        } else {
            throw new InvalidGameState("Cannot reduce parts if no parts are missing");
        }
    }

    /**
     * Modifies the number of shields that the spaceship has. The new shield value cannot be
     * below 0.
     *
     * @param value amount to adjust shields.
     * @throws SpaceShipException if the spaceship shields reach 0.
     */
    public void alterShield(int value) throws SpaceShipException {
        int newShieldValue = shieldCount + value;
        if (newShieldValue <= 0) {
            throw new SpaceShipException("No spaceship shields remaining");
        }
    }

    /**
     * Modifies the balance of the spaceship. The new balance cannot be below 0.
     *
     * @param value amount to adjust balance.
     * @throws SpaceShipException if the balance would be below 0.
     */
    public void alterSpaceBucks(int value) throws SpaceShipException {
        int newBalance = balance + value;
        if (newBalance < 0) {
            throw new SpaceShipException("Cannot remove " + value + " from Spaceship balance");
        }
        balance = newBalance;
    }

    /**
     * Returns whether or not the spaceship has crew members who can still preform actions.
     *
     * @return true if crew still have actions
     */
    public boolean hasCrewActionsRemaining() {
        for (CrewMember crewMember : this.shipCrew) {
            if (crewMember.getActionsLeftToday() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Space ships handler for the event "NEXT_DAY". Checks if the spaceship still has
     * health and crew members otherwise triggers the "DEFEAT" event.
     */
    final class NextDayHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (shipCrew.size() == 0) {
                SpaceExplorer.eventManager.notifyObservers(Event.DEFEAT,"Looks like you have run out of crew...");
            }
            if (shieldCount == 0) {
                SpaceExplorer.eventManager.notifyObservers(Event.DEFEAT,
                        "Looks like you have managed to destroy whats left of " + shipCrew);
            }
        }
    }

    /**
     * Space ships handler for the event "CREW_MEMBER_DIED". Removes the
     * dead crew member from the ship. Checks if the ship still has crew members
     * otherwise triggers the "DEFEAT" event.
     */
    final class CrewMemberDiedHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof CrewMember) {
                    shipCrew.remove(args[0]);
                }
                if (shipCrew.size() == 0) {
                    SpaceExplorer.eventManager.notifyObservers(Event.DEFEAT,"Looks like you have run out of crew...");
                }
            }
        }
    }

    /**
     * Space ships handler for the event "BUY_FROM_TRADER". Adds the item that was
     * brought to the ships items then alters the balance.
     */
    final class BuyFromTradersHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof SpaceItem) {

                    // Cannot use primitive int here as we are dealing with nulls.
                    Integer currentItemQuantity = shipItems.putIfAbsent((SpaceItem) args[0], 1);
                    if (currentItemQuantity != null) {
                        shipItems.replace((SpaceItem) args[0], currentItemQuantity + 1);
                    }

                    // Update the ships balance
                    alterSpaceBucks(((SpaceItem) args[0]).getPrice());
                }
            }

        }
    }
}
