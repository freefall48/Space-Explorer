package uc.seng201.crew.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionSleepTest {

    private GameState gameState;
    private CrewMember crewMember;

    @BeforeEach
    void init() {
        Set<Planet> planets = new HashSet<>();
        planets.add(new Planet());
        gameState = new GameState(new SpaceShip("", 1), 1, planets);
        crewMember = CrewType.HUMAN.getInstance("Test");
    }

    @DisplayName("Crew member sleep action.")
    @Test
    void sleep() {

        crewMember.alterTiredness(-90);
        IAction action = new ActionSleep();
        action.perform(gameState, null, crewMember);
        assertEquals(0 ,crewMember.getTiredness());
        assertEquals(1, crewMember.getActionsLeftToday());
    }


}