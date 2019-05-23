package uc.seng201.utils.observerable;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

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
    private Map<Event, HashSet<Observer>> observers;

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
     * @param event    that the observer wishes to subscribe to.
     * @param observer reference to the observer.
     */
    public void addObserver(Event event, Observer observer) {
        observers.get(event).add(observer);
    }

    /**
     * Removes an observer from the list of observers to be notified
     * of a certain event.
     *
     * @param event    that the observer wishes to be removed from.
     * @param observer reference to the observer to be removed.
     */
    public void removeObserver(Event event, Observer observer) {
        observers.get(event).removeIf(eventObserver -> eventObserver == observer);
    }

    /**
     * Notifies all observers of a given event.
     *
     * @param event notify observers of this event.
     * @param args  to be passed to each observer.
     */
    public void notifyObservers(Event event, Object... args) {

        /*
        Make a local copy of the observers for the given event. If we dont and
        an observer removes itself during the iteration we would get a concurrent
        error.
         */
        Set<Observer> eventObservers = new HashSet<>(observers.get(event));

        eventObservers.forEach(observer -> observer.onEvent(args));
    }
}
