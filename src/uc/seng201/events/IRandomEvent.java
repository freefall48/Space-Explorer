package uc.seng201.events;

import uc.seng201.SpaceShip;
import uc.seng201.helpers.Helpers;

import java.util.ArrayList;
import java.util.List;

public interface IRandomEvent {

    void onTrigger(SpaceShip spaceShip);

    static RandomEvent eventToTrigger(EventTrigger eventTrigger) {
        List<RandomEvent> possibleEvents = new ArrayList<>();
        switch (eventTrigger) {
            case START_DAY:
                for (RandomEvent event : RandomEvent.values()) {
                    if (event.getTrigger().equals(EventTrigger.START_DAY)) {
                        possibleEvents.add(event);
                    }
                }
                break;
            case TRAVEL:
                for (RandomEvent event : RandomEvent.values()) {
                    if (event.getTrigger().equals(EventTrigger.TRAVEL)) {
                        possibleEvents.add(event);
                    }
                }
                break;
        }
        return possibleEvents.get(Helpers.randomGenerator.nextInt(possibleEvents.size()));
    }
}
