package uc.seng201.crew.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Human;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionSearchTest {


    private GameState gameState;
    private CrewMember crewMember;

    @BeforeEach
    void init() {
        Set<Planet> planets = new HashSet<>();
        planets.add(new Planet());
        gameState = new GameState(new SpaceShip("", 1), 1, planets);
        crewMember = new Human("Test");
    }

    // This test does reply on randomness.
    @DisplayName("Searching a planet all outcomes are possible.")
    @Test
    void search() {
        List<String> outcomes = new ArrayList<>();
        int loops = 50;
        crewMember.alterActionsLeft(loops);
        // There are 4 outcomes so they should of all occurred
        for (int i = 0; i < loops; i++) {
            String message = new ActionSearch().perform(gameState, new Object[]{gameState.getCurrentPlanet()}, crewMember);
            if (!outcomes.contains(message)) {
                outcomes.add(message);
            }
        }

        // Check if nothing could be found.
        String nothingFound = "Unfortunately Test did not find anything this time.";
        assertTrue(outcomes.contains(nothingFound));
        // Check if a spaceship part could be found.
        String partFound = "TEST FOUND A PART OF THE SHIP!";
        assertTrue(outcomes.contains(partFound));
        // Check if an item was found.
        assertTrue(() -> {
            for (String string : outcomes) {
                if (string.contains("item")) {
                    return true;
                }
            }
            return false;
        });
        // Check if money was found.
        assertTrue(() -> {
            for (String string: outcomes) {
                if (string.contains("$")) {
                    return true;
                }
            }
            return false;
        });

        // We would expect 2 as we added the number of actions points we intended to use.
        assertEquals(2, crewMember.getActionsLeftToday());
    }
}
