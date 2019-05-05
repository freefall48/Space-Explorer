package uc.seng201.events;

/**
 * Possible random destinations that can occur during the game.
 */
public enum RandomEvent {
    ALIEN_PIRATES(EventTrigger.START_DAY,"Oh no! Space pirates have boarded and stolen an item!") {
        @Override
        public IRandomEvent getInstance() {
            return new EventAlienPirates();
        }
    },
    SPACE_PLAGUE(EventTrigger.START_DAY,"Err...Some crew members don't look so healthy!") {
        @Override
        public IRandomEvent getInstance() {
            return new EventSpacePlague();
        }
    },
    ASTEROID_BELT(EventTrigger.TRAVEL, "Oh no! We are traveling through an asteroid belt!") {
        @Override
        public IRandomEvent getInstance() {
            return new EventAsteroidBelt();
        }
    };

    private EventTrigger trigger;
    private String eventDescription;

    RandomEvent(EventTrigger trigger, String eventDescription) {
        this.trigger = trigger;
        this.eventDescription = eventDescription;
    }



    public EventTrigger getTrigger() {
        return this.trigger;
    }

    public String getEventDescription() {
        return this.eventDescription;
    }

    public abstract IRandomEvent getInstance();

}
