package uc.seng201.crew.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Human;
import uc.seng201.environment.GameState;
import uc.seng201.items.SpaceItem;
import uc.seng201.misc.Planet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActionConsumeItemTest {

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
        gameState.getSpaceShip().add(SpaceItem.CHICKEN);
        gameState.getSpaceShip().add(SpaceItem.PORK);

    }

    @DisplayName("Action consume item.")
    @Test
    void perform() {
        new ActionConsumeItem().perform(gameState, new Object[]{SpaceItem.CHICKEN}, new Human("test"));
        assertEquals(new HashMap<SpaceItem, Integer>(){{put(SpaceItem.PORK, 1);}},
                gameState.getSpaceShip().getShipItems());
    }
}