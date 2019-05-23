package uc.seng201.utils.observerable;

/**
 * Events that can be observed. When triggering a event make sure
 * the requirements for the event arguments are met. Failing to do
 * so may cause some handlers to handle the event incorrectly.
 */
public enum Event {
    /**
     * Event is called when the game state should start a new day.
     * The event must contain only a reference to the current game state
     * as its first argument.
     */
    START_DAY,
    /**
     * Event is called when a random event should be triggered. The
     * event must contain the a "RandomEventType" as its first argument
     * and a reference to the current game state state as its second.
     */
    RANDOM_EVENT,
    /**
     * Event is called when a crew member dies (0 Health). The event
     * must have the "CrewMember" who died as its first and only
     * argument.
     */
    CREW_MEMBER_DIED,
    /**
     * Event is called when a crew member has performed an action. The
     * event must have the action being performed, the crew who are performing
     * the action and the action specific arguments.
     */
    CREW_MEMBER_ACTION,
    /**
     * Event is called when the user trades with the space traders. The
     * event must have the "SpaceItem" that the user is trying to buy
     * as its one and only argument.
     */
    BUY_FROM_TRADERS,
    /**
     * Event is called when the user wins the game. The event must contain
     * a "String" that describes hows the user won the game as its one
     * and only argument.
     */
    VICTORY,
    /**
     * Event is called when the user loses the game. The event must
     * contains a "String" that describes how the user lost the game
     * as its one and only argument.
     */
    DEFEAT,
    /**
     * Event is called when a new game state is available. The event must
     * contain the new game state as its one and only argument.
     */
    NEW_GAME_STATE,
    /**
     * Event is called when a game state is loaded from file. The event
     * must contain the loaded game state as its one and only argument.
     */
    LOADED_GAME_STATE,
    ACTION
}
