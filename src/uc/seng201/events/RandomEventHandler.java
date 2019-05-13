package uc.seng201.events;

import uc.seng201.Display;
import uc.seng201.GameState;
import uc.seng201.utils.Helpers;
import uc.seng201.utils.observerable.Observer;

import java.util.ArrayList;
import java.util.List;

public class RandomEventHandler implements Observer {

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
                String message = possibleEvents.get(Helpers.randomGenerator.nextInt(possibleEvents.size()))
                        .getInstance().onTrigger((GameState) args[1]);
                Display.popup(message);
            }
        }
    }
}
