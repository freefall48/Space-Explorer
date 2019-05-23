package uc.seng201.crew.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionRepairTest {

    private GameState gameState;
    private CrewMember crewMember;

    @BeforeEach
    void init() {
        Set<Planet> planets = new HashSet<>();
        planets.add(new Planet());
        gameState = new GameState(new SpaceShip("", 1), 1, planets);
        crewMember = CrewType.HUMAN.getInstance("Test");
    }

    @DisplayName("Repair action correctly repairs the ship.")
    @ParameterizedTest(name = "{index} => value={0}, result={1}")
    @CsvSource({
            "100,97",
            "40, 100",
            "70, 100"
    })
    void perform(int value, int result) {
        gameState.getSpaceShip().damage(value);
        new ActionRepair().perform(gameState, null, crewMember);
        assertEquals(result, gameState.getSpaceShip().getShipHealth());
        assertEquals(1, crewMember.getActionsLeftToday());
    }
}