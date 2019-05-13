package uc.seng201.utils.observerable;

/**
 * Interface for event observers to implement. Event observers of this
 * type can then be given to an observer manager.
 */
public interface Observer {

    /**
     * Called when the event that it is subscribed to is triggered. The
     * args should match that of the event that it is subscribed to and
     * is not expected to handle different args.
     *
     * @param args passed to the observers by the event caller.
     */
    void onEvent(Object... args);
}
