package uc.seng201.crew.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Human;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActionPilotTest {
    private GameState gameState;
    private Planet destination = new Planet();

    @BeforeEach
    void init() {
        Set<Planet> planets = new HashSet<>();
        Planet current = new Planet();
        planets.add(current);
        planets.add(destination);
        gameState = new GameState(new SpaceShip("", 1), 1, planets);
        gameState.setCurrentPlanet(current);

    }

    @DisplayName("Pilot action changes planet")
    @Test
    void perform() {
        CrewMember crew1 = new Human("test1");
        CrewMember crew2 = new Human("test2");
        new ActionPilot().perform(gameState, new Object[]{destination},
                crew1, crew2);
        assertEquals(destination, gameState.getCurrentPlanet());
        assertEquals(1, crew1.getActionsLeftToday());
        assertEquals(1, crew2.getActionsLeftToday());
    }
}