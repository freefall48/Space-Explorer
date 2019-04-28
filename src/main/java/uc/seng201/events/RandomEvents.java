package uc.seng201.events;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.modifers.Illnesses;
import uc.seng201.helpers.Helpers;

/**
 * Possible random targets that can occur during the game.
 */
public enum RandomEvents {
    ALIEN_PIRATES(EventTrigger.START_DAY,"Oh no! Space pirates have boarded %s and stolen an item!") {
        @Override
        public void onTrigger(SpaceShip spaceShip) {
            if (spaceShip.getShipItems().size() > 0) {
                int positionToRemove = Helpers.randomGenerator.nextInt(spaceShip.getShipItems().size());
                spaceShip.getShipItems().remove(positionToRemove);
            }
        }
    },
    SPACE_PLAGUE(EventTrigger.START_DAY,"Err...Some crew members don't look so healthy!") {
        @Override
        public void onTrigger(SpaceShip spaceShip) {
            for (CrewMember crewMember : spaceShip.getShipCrew(true)) {
                if (Helpers.randomGenerator.nextBoolean()) {
                    crewMember.addIllness(Illnesses.SPACE_PLAGUE);
                }
            }
        }
    },
    ASTEROID_BELT(EventTrigger.TRAVEL, "Oh no! %s is traveling through an asteroid belt! %s and %s" +
            "should be more careful next time.") {
        @Override
        public void onTrigger(SpaceShip spaceShip) {
            spaceShip.alterShield(-1);
        }
    };

    private EventTrigger trigger;
    private String eventDescription;

    RandomEvents(EventTrigger trigger, String eventDescription) {
        this.trigger = trigger;
        this.eventDescription = eventDescription;
    }

    public EventTrigger getTrigger() {
        return this.trigger;
    }

    public String getEventDescription() {
        return this.eventDescription;
    }

    public abstract void onTrigger(SpaceShip spaceShip);

}
