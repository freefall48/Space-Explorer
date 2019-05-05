package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.destinations.Planet;
import uc.seng201.errors.ActionException;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.IRandomEvent;
import uc.seng201.events.RandomEvent;
import uc.seng201.helpers.Helpers;

import javax.swing.*;

public class ActionPilot implements IAction {
    @Override
    public String perform(GameState gameState, Object[] args, CrewMember... crewMembers) {
        if (args.length != 1 && crewMembers.length == 2) {
            throw new ActionException("Invalid args size");
        }
        if (args[0] instanceof Planet) {
            gameState.setCurrentPlanet((Planet) args[0]);
            if (Helpers.randomGenerator.nextBoolean()) {
                RandomEvent event = IRandomEvent.eventToTrigger(EventTrigger.TRAVEL);
                event.getInstance().onTrigger(gameState.getSpaceShip());
                JOptionPane.showMessageDialog(null , event.getEventDescription());
            }
        } else {
            throw new ActionException("Invalid arg passed");
        }
        return null;
    }
}
