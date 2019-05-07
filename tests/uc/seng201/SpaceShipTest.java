package uc.seng201;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class SpaceShipTest {

    private SpaceShip spaceShip;

    @BeforeEach
    private void createSpaceShip() {
        spaceShip = new SpaceShip("Testing", 3);
    }

    @DisplayName("Should add crew members correctly.")
    @ParameterizedTest
    @MethodSource("addCrewProvider")
    void add() {
        List<CrewMember> crewMembers = new ArrayList<>();
        crewMembers.add(new CrewMember("Test", CrewType.HUMAN));
        crewMembers.add(new CrewMember("Test2", CrewType.LANIUS));

        spaceShip.add(crewMembers);

        Assertions.assertEquals(spaceShip.getShipCrew(), crewMembers);
    }

    private static Stream<Arguments> addCrewProvider() {
        List<CrewMember> one = new ArrayList<>();
        List<CrewMember> two = new ArrayList<>();
        two.add()
        return Stream.of(
                Arguments.of(one),
                Arguments.of(two)
        );
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
    void getMissingPartsAtStart() {
    }

    @Test
    void remove() {
    }

    @Test
    void remove1() {
    }

    @Test
    void getShipName() {
    }

    @Test
    void getShipCrew() {
    }

    @Test
    void getShipItems() {
    }

    @Test
    void getSpaceBucks() {
    }

    @Test
    void getShieldCount() {
    }

    @Test
    void getMissingParts() {
    }

    @Test
    void partFound() {
    }

    @Test
    void alterShield() {
    }

    @Test
    void alterSpaceBucks() {
    }

    @Test
    void hasCrewActionsRemaining() {
    }
}