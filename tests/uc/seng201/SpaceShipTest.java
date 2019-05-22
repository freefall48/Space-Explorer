package uc.seng201;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uc.seng201.crew.*;
import uc.seng201.errors.SpaceShipException;
import uc.seng201.items.SpaceItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip;

    @BeforeEach
    void init(){
        spaceShip = new SpaceShip("", 4);
    }

    @DisplayName("Add too few crew members.")
    @Test
    void add() {
        Set<CrewMember> testCrew = new HashSet<>();
        testCrew.add(new Human("Test1"));
        assertFalse(spaceShip.add(testCrew));
    }

    @DisplayName("Add too many crew.")
    @Test
    void add1() {
        Set<CrewMember> testCrew = new HashSet<>();
        testCrew.add(new Human("Test1"));
        testCrew.add(new Human("Test2"));
        testCrew.add(new Human("Test3"));
        testCrew.add(new Human("Test4"));
        testCrew.add(new Human("Test5"));
        assertFalse(spaceShip.add(testCrew));
    }

    @DisplayName("Add too many crew.")
    @Test
    void add2() {
        Set<CrewMember> testCrew = new HashSet<>();
        testCrew.add(new Human("Test1"));
        testCrew.add(new Human("Test2"));
        testCrew.add(new Human("Test3"));
        spaceShip.add(testCrew);
        assertEquals(testCrew, spaceShip.getShipCrew());
    }

    @DisplayName("Get a crew member by their name and type when present.")
    @Test
    void crewMemberFromName() {
        Set<CrewMember> testCrew = new HashSet<>();
        CrewMember crewToFind = new Engi("Test1");
        testCrew.add(new Human("Test1"));
        testCrew.add(crewToFind);
        testCrew.add(new Crystal("Test1"));
        spaceShip.add(testCrew);
        assertEquals(crewToFind, spaceShip.crewMemberFromNameAndType("Test1", "Engi"));
    }

    @DisplayName("Get a crew member by their name and type when not present.")
    @Test
    void crewMemberFromName1() {
        Set<CrewMember> testCrew = new HashSet<>();
        testCrew.add(new Human("Test1"));
        testCrew.add(new Crystal("Test1"));
        spaceShip.add(testCrew);
        assertNull(spaceShip.crewMemberFromNameAndType("Test1", "Engi"));
    }

    @DisplayName("Remove a space item that is present.")
    @Test
    void remove() {
        SpaceItem testItem = SpaceItem.PORK;
        spaceShip.add(testItem);
        spaceShip.add(SpaceItem.CHICKEN);
        SpaceItem itemRemoved = spaceShip.remove(testItem);
        assertEquals(testItem, itemRemoved);
    }

    @DisplayName("Remove a space item when not present.")
    @Test
    void remove1() {
        spaceShip.add(SpaceItem.CHICKEN);
        assertNull(spaceShip.remove(SpaceItem.PORK));
    }

    @DisplayName("Damage spaceship with health and shields.")
    @Test
    void damage() {
        int damageToDeal = 50;
        int expectedHealth = spaceShip.getShipHealth() - (damageToDeal / (spaceShip.getShieldCount()));
        spaceShip.damage(damageToDeal);
        assertEquals(expectedHealth, spaceShip.getShipHealth());
    }

    @DisplayName("Damage to spaceship with health and no shields.")
    @Test
    void damage1() {
        int startingShields = spaceShip.getShieldCount();
        for (int i = 0; i < startingShields; i++) {
            spaceShip.damage(1);
        }
        assertEquals(0, spaceShip.getShieldCount());

        int damageToDeal = 10;
        int expectedHealth = spaceShip.getShipHealth()- damageToDeal;
        spaceShip.damage(damageToDeal);
        assertEquals(expectedHealth, spaceShip.getShipHealth());
    }

    @DisplayName("Repair space ship health.")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "10,77",
            "-10, 67",
            "100, 100"
    })
    void repair(int value, int result) {
        spaceShip.damage(100);
        spaceShip.repair(value);
        assertEquals(result, spaceShip.getShipHealth());
    }

    @DisplayName("Remove below 0 balance.")
    @Test
    void alterSpaceBucks() {
        assertThrows(SpaceShipException.class,() -> spaceShip.alterSpaceBucks(-200));
    }

    @DisplayName("Add and remove from the balance.")
    @Test
    void alterSpaceBucks1() {
        spaceShip.alterSpaceBucks(100);
        assertEquals(100, spaceShip.getBalance());
        spaceShip.alterSpaceBucks(-20);
        assertEquals(80, spaceShip.getBalance());
    }

    @DisplayName("Crew member actions remaining for the day.")
    @Test
    void actionsRemaining() {
        Set<CrewMember> testCrew = new HashSet<>();
        testCrew.add(new Human("Test1"));
        testCrew.add(new Crystal("Test1"));
        spaceShip.add(testCrew);

        assertTrue(spaceShip.hasCrewActionsRemaining());
        spaceShip.getShipCrew().forEach(crewMember -> {
            while (crewMember.getActionsLeftToday() > 0) {
                crewMember.performAction();
            }
        });
        assertFalse(spaceShip.hasCrewActionsRemaining());

    }
}