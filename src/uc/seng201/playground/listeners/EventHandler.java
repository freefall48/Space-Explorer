package uc.seng201.playground.listeners;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class EventHandler {

    private Map<Events, List<Observer>> observers;

    public EventHandler() {
        observers = new Hashtable<>();
        for (Events event : Events.values()) {
            observers.put(event, new ArrayList<>());
        }
    }

    public void addObserver(Events event, Observer observer) {
        observers.get(event).add(observer);
    }

    public void removeObserver(Events event, Observer observer) {
        observers.get(event).remove(observer);
    }

    public void notifyObservers(Events event) {
        for (Observer observer : observers.get(event)) {
            observer.onEvent();
        }
    }
}
