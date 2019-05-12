package uc.seng201;

import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.ObservableHandler;
import uc.seng201.utils.observerable.Observer;

public class EventHandler extends ObservableHandler {

    public EventHandler() {
        super();
    }

    @Override
    public void addObserver(Event event, Observer observer) {
        super.addObserver(event, observer);
    }

    @Override
    public void removeObserver(Event event, Observer observer) {
        super.removeObserver(event, observer);
    }

    @Override
    public void notifyObservers(Event event, Object... args) {
        super.notifyObservers(event, args);
    }
}
