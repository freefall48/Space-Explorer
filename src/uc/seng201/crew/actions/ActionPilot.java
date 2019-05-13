package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.destinations.Planet;
import uc.seng201.errors.ActionException;
import uc.seng201.events.EventTrigger;
import uc.seng201.utils.Helpers;
import uc.seng201.utils.observerable.Event;

public class ActionPilot implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        if (args.length != 1 && crewMembers.length == 2) {
            throw new ActionException("Invalid args size");
        }
        if (args[0] instanceof Planet) {
            gameState.setCurrentPlanet((Planet) args[0]);
            if (Helpers.randomGenerator.nextBoolean()) {
                SpaceExplorer.eventManager.notifyObservers(Event.RANDOM_EVENT, EventTrigger.TRAVEL);
            }
        } else {
            throw new ActionException("Invalid arg passed");
        }
        return null;
    }
}
