package uc.seng201;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Human;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip spaceShip;

    @BeforeEach
    void init(){
        spaceShip = new SpaceShip("", 4);
    }

    @DisplayName("Add crew members")
    @Test
    void add() {

    }

    @Test
    void add1() {
    }

    @Test
    void crewMemberFromName() {
    }

    @Test
    void contains() {
    }

    @Test
    void contains1() {
    }

    @Test
    void remove() {
    }

    @Test
    void remove1() {
    }

    @Test
    void alterShield() {
    }

    @Test
    void alterSpaceBucks() {
    }

    @DisplayName("Has crew actions remaining")
    @Test
    void hasCrewActionsRemaining() {
        List<CrewMember> crew = new ArrayList<>();
        crew.add(new Human(""));
        Human crewMember = new Human("");
        crewMember.setActionsLeftToday(0);
        crew.add(crewMember);
        spaceShip.add(crew);
        assertTrue(spaceShip.hasCrewActionsRemaining());
    }
}