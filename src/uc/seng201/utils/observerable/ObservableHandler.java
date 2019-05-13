package uc.seng201.utils.observerable;

import java.util.*;

public abstract class ObservableHandler {

    private Map<Event, Set<Observer>> observers;

    public ObservableHandler() {
        observers = new Hashtable<>();

        for (Event event : Event.values()) {
            observers.put(event, new HashSet<>());
        }
    }

    public void addObserver(Event event, Observer observer) {
        Set<Observer> eventObservers = observers.get(event);
        eventObservers.add(observer);
//        observers.replace(event, eventObservers);
    }

    public void removeObserver(Event event, Observer observer) {
        observers.get(event).remove(observer);
    }

    public void notifyObservers(Event event, Object... args) {
        for (Observer observer : observers.get(event)) {
            observer.onEvent(args);
        }
    }
}
