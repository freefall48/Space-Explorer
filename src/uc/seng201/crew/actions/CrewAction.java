package uc.seng201.crew.actions;

public enum CrewAction {

    PILOT("<html>%s and %s will fly %s to %s!", 2, true),
    SEARCH("<html>%s will search the current planet '%s'.<br>%s", 1, true),
    SLEEP("<html>%s will sleep to remove tiredness.", 1, true),
    EAT("<html>%s will eat %s", 1, true),
    MEDICAL("<html>%s will use %s", 1, true),
    REPAIR("<html>%s will repair %s", 1, true);

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
}
