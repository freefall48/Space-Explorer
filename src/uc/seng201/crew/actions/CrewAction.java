package uc.seng201.crew.actions;

/**
 * All actions that can be performed by a crew member.
 */
public enum CrewAction {
    /**
     * Two crew members can pilot the space ship to a new planet.
     */
    PILOT("<html>%s and %s will fly %s to %s!", 2) {
        @Override
        public IAction getInstance() {
            return new ActionPilot();
        }
    },
    /**
     * A crew member can search the currently orbited planet for items,
     * money or spaceship parts. Many not find anything. Only one part
     * per planet.
     */
    SEARCH("<html>%s will search the current planet '%s'.<br>%s", 1) {
        @Override
        public IAction getInstance() {
            return new ActionSearch();
        }
    },
    /**
     * A crew member can sleep to reduce their current tiredness level.
     */
    SLEEP("<html>%s will sleep to remove tiredness.", 1) {
        @Override
        public IAction getInstance() {
            return new ActionSleep();
        }
    },
    /**
     * A crew member can consume an item that is currently on the spaceship.
     */
    CONSUME("<html>%s will consume the %s", 1) {
        @Override
        public IAction getInstance() {
            return new ActionConsumeItem();
        }
    },
    /**
     * A crew member can repair damage that the spaceship has taken.
     */
    REPAIR("<html>%s will repair %s", 1) {
        @Override
        public IAction getInstance() {
            return new ActionRepair();
        }
    };

    /**
     * The message to be displayed when informing the user what the action
     * will do. Formatters can be used and replaced when displayed to
     * the user.
     */
    private String actionText;
    /**
     * How many crew members does the action require to perform.
     */
    private int crewRequired;

    /**
     * Provides an available action for a crew member to perform.
     *
     * @param actionText message to be displayed describing the action.
     * @param crewRequired how many crew are needed to perform.
     */
    CrewAction(final String actionText, final int crewRequired) {
        this.actionText = actionText;
        this.crewRequired = crewRequired;
    }

    /**
     * Returns the message that should be displayed to the user
     * to describe the action. Placeholder should be replaced with
     * values.
     *
     * @return message to be displayed.
     */
    public String getActionText() {
        return actionText;
    }

    /**
     * Returns the number of crew members that are required to
     * perform the action.
     *
     * @return number of crew needed to perform the action.
     */
    public int getCrewRequired() {
        return crewRequired;
    }

    /**
     * Returns an instance of the action that the enum value represents.
     *
     * @return instance of an IAction.
     */
    public abstract IAction getInstance();
}
