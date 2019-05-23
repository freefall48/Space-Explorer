package uc.seng201.misc.events;

/**
 * Possible random misc that can occur during the game.
 */
public enum RandomEventType {
    /**
     * Alien pirates bored the ship and attempt to steal
     * an item.
     */
    ALIEN_PIRATES(EventTrigger.START_DAY) {
        @Override
        public IRandomEvent getInstance() {
            return new EventAlienPirates();
        }
    },
    /**
     * Crew members become infected by the space plaque.
     */
    SPACE_PLAGUE(EventTrigger.START_DAY) {
        @Override
        public IRandomEvent getInstance() {
            return new EventSpacePlague();
        }
    },
    /**
     * The space ship takes damage based when flown
     * through an asteroid belt.
     */
    ASTEROID_BELT(EventTrigger.TRAVEL) {
        @Override
        public IRandomEvent getInstance() {
            return new EventAsteroidBelt();
        }
    };

    /**
     * Type of event trigger.
     */
    private EventTrigger trigger;

    /**
     * Constructs event.
     *
     * @param trigger that the event can be caused by.
     */
    RandomEventType(EventTrigger trigger) {
        this.trigger = trigger;
    }


    /**
     * Returns event trigger type.
     *
     * @return trigger of the event.
     */
    public EventTrigger getTrigger() {
        return this.trigger;
    }

    /**
     * Gets instance of random event that corresponds to the enum
     * type.
     *
     * @return instance of random event.
     */
    public abstract IRandomEvent getInstance();

}
