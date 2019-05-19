package uc.seng201.environment;

import uc.seng201.SpaceExplorer;
import uc.seng201.misc.events.EventTrigger;
import uc.seng201.misc.events.RandomEventType;
import uc.seng201.utils.observerable.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles random events that may occur during the play through of the game.
 */
public class RandomEventHandler implements Observer {

    /**
     * Checks to see what type of random event should be triggered and alters the game state to
     * reflect these changes.
     *
     * @param args passed to the observers by the event caller.
     */
    @Override
    public void onEvent(Object... args) {

        List<RandomEventType> possibleEvents = new ArrayList<>();
        if (args.length == 2) {
            if (args[0] instanceof EventTrigger && args[1] instanceof GameState) {
                switch ((EventTrigger) args[0]) {
                    case START_DAY:
                        for (RandomEventType event : RandomEventType.values()) {
                            if (event.getTrigger().equals(EventTrigger.START_DAY)) {
                                possibleEvents.add(event);
                            }
                        }
                        break;
                    case TRAVEL:
                        for (RandomEventType event : RandomEventType.values()) {
                            if (event.getTrigger().equals(EventTrigger.TRAVEL)) {
                                possibleEvents.add(event);
                            }
                        }
                        break;
                }
                String message = possibleEvents.get(SpaceExplorer.randomGenerator.nextInt(possibleEvents.size()))
                        .getInstance().onTrigger((GameState) args[1]);
                Display.popup(message);
                return;
            }
        }
        throw new IllegalArgumentException();
    }
}
