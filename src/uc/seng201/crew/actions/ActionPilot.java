package uc.seng201.crew.actions;

import uc.seng201.environment.GameEnvironment;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.misc.Planet;
import uc.seng201.errors.ActionException;
import uc.seng201.misc.events.EventTrigger;
import uc.seng201.utils.observerable.Event;

public class ActionPilot implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {

        // Check we have got arguments we need.
        if (args.length != 1 && crewMembers.length == 2) {
            throw new ActionException("Invalid args size");
        }
        if (args[0] instanceof Planet) {
            gameState.setCurrentPlanet((Planet) args[0]);

            /*
             Checks if the random event should occur after this action has been performed. If it is to
             occur, notify the event manager.
             */
            if (SpaceExplorer.randomGenerator.nextBoolean()) {
                GameEnvironment.eventManager.notifyObservers(Event.RANDOM_EVENT, EventTrigger.TRAVEL);
            }
        } else {
            throw new ActionException("Invalid arg passed");
        }
        return null;
    }
}
