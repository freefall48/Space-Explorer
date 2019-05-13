package uc.seng201.utils.observerable;

import java.util.*;

/**
 * Contains implementation for an ObservableManager object. Does
 * not reference java.lang.ObservableManager as it is deprecated.
 */
public class ObservableManager {

    /**
     * Map of events and their observers. A set is used for
     * the observers to prevent the same observer watching the
     * same event multiple times.
     */
    private Map<Event, Set<Observer>> observers;

    /**
     * Initializes the observers as a new hashtable. For each event their known
     * observers is initialised to an empty HashSet.
     */
    public ObservableManager() {
        observers = new Hashtable<>();

        for (Event event : Event.values()) {
            observers.put(event, new HashSet<>());
        }
    }

    /**
     * Adds an observer to the list of observers to be notified
     * of a certain event.
     *
     * @param event that the observer wishes to subscribe to.
     * @param observer reference to the observer.
     */
    public void addObserver(Event event, Observer observer) {
        Set<Observer> eventObservers = observers.get(event);
        eventObservers.add(observer);
    }

    /**
     * Removes an observer from the list of observers to be notified
     * of a certain event.
     *
     * @param event that the observer wishes to be removed from.
     * @param observer reference to the observer to be removed.
     */
    public void removeObserver(Event event, Observer observer) {
        observers.get(event).remove(observer);
    }

    /**
     * Notifies all observers of a given event.
     *
     * @param event notify observers of this event.
     * @param args to be passed to each observer.
     */
    public void notifyObservers(Event event, Object... args) {
        for (Observer observer : observers.get(event)) {
            observer.onEvent(args);
        }
    }
}
