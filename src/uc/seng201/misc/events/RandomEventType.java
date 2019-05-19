package uc.seng201.misc.events;

/**
 * Possible random misc that can occur during the game.
 */
public enum RandomEventType {
    ALIEN_PIRATES(EventTrigger.START_DAY) {
        @Override
        public IRandomEvent getInstance() {
            return new EventAlienPirates();
        }
    },
    SPACE_PLAGUE(EventTrigger.START_DAY) {
        @Override
        public IRandomEvent getInstance() {
            return new EventSpacePlague();
        }
    },
    ASTEROID_BELT(EventTrigger.TRAVEL) {
        @Override
        public IRandomEvent getInstance() {
            return new EventAsteroidBelt();
        }
    };

    private EventTrigger trigger;

    RandomEventType(EventTrigger trigger) {
        this.trigger = trigger;
    }



    public EventTrigger getTrigger() {
        return this.trigger;
    }

    public abstract IRandomEvent getInstance();

}
