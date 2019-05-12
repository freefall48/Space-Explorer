package uc.seng201.crew;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import uc.seng201.errors.CrewMemberException;

import static org.junit.jupiter.api.Assertions.*;

class CrewMemberTest {

    private CrewMember crewMember;

    @DisplayName("Correct crew member creation")
    @ParameterizedTest
    @EnumSource(CrewType.class)
    void creation(CrewType crewType) {
        CrewMember crewMember = crewType.getInstance("");
        assertEquals(crewType, crewMember.getCrewType());
    }

    @BeforeEach
    void init() {
        crewMember = new Human("");
    }

    @DisplayName("Increase food level")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "10, 10",
            "100, 100",
            "1000, 100",
            "0,0"
    })
    void alterFood(int value, int result) {
        crewMember.alterFood(-100);
        crewMember.alterFood(value);
        assertEquals(result, crewMember.getFoodLevel());
    }

    @DisplayName("Decrease food level")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "-10, 90",
            "-100, 0",
            "-1000, 0",
            "0, 100"
    })
    void alterFood1(int value, int result) {
        crewMember.alterFood(value);
        assertEquals(result, crewMember.getFoodLevel());
    }

    @DisplayName("Increase health")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "1000, 100",
            "100,100",
            "10, 10",
            "0, 0"
    })
    void alterHealth(int value, int result) {
        crewMember.alterHealth(-100);
        crewMember.alterHealth(value);
        assertEquals(result, crewMember.getHealth());
    }

    @DisplayName("Decrease Health")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "-1000, 0",
            "-100,0",
            "-10, 90",
    })
    void alterHealth1(int value, int result) {
        crewMember.alterHealth(value);
        assertEquals(result, crewMember.getHealth());
    }

    @DisplayName("Increase tiredness")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "0,0",
            "10, 10",
            "100, 100",
            "1000,100"
    })
    void alterTiredness(int value, int result) {
        crewMember.alterTiredness(value);
        assertEquals(result, crewMember.getTiredness());
    }

    @DisplayName("Decrease tiredness")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "-1000, 0",
            "-100,0",
            "-10, 90",
            "0,100"
    })
    void alterTiredness1(int value, int result) {
        crewMember.alterTiredness(100);
        crewMember.alterTiredness(value);
        assertEquals(result, crewMember.getTiredness());
    }

    @DisplayName("Can perform actions")
    @ParameterizedTest
    @ValueSource(ints = {1,2})
    void canPerformActions() {
        crewMember.setActionsLeftToday(1);
        assertTrue(crewMember.canPerformActions());
    }

    @DisplayName("Cannot perform actions")
    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    void canPerformActions1() {
        crewMember.setActionsLeftToday(0);
        assertFalse(crewMember.canPerformActions());
    }

    @DisplayName("Altering a valid action count")
    @ParameterizedTest
    @ValueSource(ints = {2, 1})
    void performAction(int crewActions) {
        crewMember.setActionsLeftToday(crewActions);
        crewMember.performAction();
        assertEquals(crewActions - 1, crewMember.getActionsLeftToday());
    }

    @DisplayName("Altering an invalid action count")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void performAction1(int crewActions) {
        crewMember.setActionsLeftToday(crewActions);
        assertThrows(CrewMemberException.class, crewMember::performAction);
    }


    @DisplayName("Crew member not alive")
    @Test
    void isAlive() {
        crewMember.alterHealth(-100);
        assertFalse(crewMember.isAlive());
    }
    @DisplayName("Crew member is alive")
    @Test
    void isAlive1() {
        assertTrue(crewMember.isAlive());
    }
}