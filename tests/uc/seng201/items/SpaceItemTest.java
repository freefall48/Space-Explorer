package uc.seng201.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.crew.modifers.Modifications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SpaceItemTest {

    private CrewMember crewMember;

    @BeforeEach
    void setup() {
        crewMember = CrewType.HUMAN.getInstance("Test");
    }

    @DisplayName("Items with no overwritten onConsume()")
    @Test
    void standardItem() {
        crewMember.alterFood(-80);
        // Crew member is now hungry and will be affected by items.
        SpaceItem item = SpaceItem.CARROT;
            item.onConsume(crewMember);
            int expectedFood = 20 + (item.getPrice() * 2);
            assertEquals(expectedFood, crewMember.getFoodLevel());

    }

    @DisplayName("Stardew removes space plague from a crew member.")
    @Test
    void stardew() {
        crewMember.addModification(Modifications.SPACE_PLAGUE);
        SpaceItem.STARDEW.onConsume(crewMember);
        assertFalse(crewMember.getModifications().contains(Modifications.SPACE_PLAGUE));
    }

    @DisplayName("Medical items restore health with overridden onConsume()")
    @Test
    void healingItems() {
        crewMember.alterHealth(-60);
        SpaceItem.BANDAGE.onConsume(crewMember);
        assertEquals(90, crewMember.getHealth());
    }
}