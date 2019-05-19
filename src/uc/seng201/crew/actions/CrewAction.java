package uc.seng201.crew.actions;

public enum CrewAction {

    PILOT("<html>%s and %s will fly %s to %s!", 2, true) {
        @Override
        public IAction getInstance() {
            return new ActionPilot();
        }
    },
    SEARCH("<html>%s will search the current planet '%s'.<br>%s", 1, true) {
        @Override
        public IAction getInstance() {
            return new ActionSearch();
        }
    },
    SLEEP("<html>%s will sleep to remove tiredness.", 1, true) {
        @Override
        public IAction getInstance() {
            return new ActionSleep();
        }
    },
    CONSUME("<html>%s will consume the %s", 1, true) {
        @Override
        public IAction getInstance() {
            return new ActionConsumeItem();
        }
    },
    REPAIR("<html>%s will repair %s", 1, true) {
        @Override
        public IAction getInstance() {
            return new ActionRepair();
        }
    };

    private String actionText;
    private int crewRequired;
    private boolean costsActionPoint;

    CrewAction(String actionText, int crewRequired, boolean costsActionPoint) {
        this.actionText = actionText;
        this.crewRequired = crewRequired;
        this.costsActionPoint = costsActionPoint;
    }
    public String getActionText() {
        return this.actionText;
    }

    public int getCrewRequired() {
        return this.crewRequired;
    }

    public boolean getCostsActionPoint() {
        return this.costsActionPoint;
    }

    /**
     * Returns an instance of the action that the enum value represents.\
     *
     * @return instance of an IAction.
     */
    public abstract IAction getInstance();
}
