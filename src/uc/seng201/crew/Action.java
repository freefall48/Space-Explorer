package uc.seng201.crew;

public enum Action {

    PILOT("%s and %s will fly %s to %s!", 2, true),
    SEARCH("%s will search the current planet '%s'.\nShip part found here? %b", 1, true),
    SLEEP("%s will sleep to remove tiredness.", 1, true),
    EAT("%s will eat %s", 1, false);


    private String actionText;
    private int crewRequired;
    private boolean costsActionPoint;

    Action(String actionText, int crewRequired, boolean costsActionPoint) {
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
}
