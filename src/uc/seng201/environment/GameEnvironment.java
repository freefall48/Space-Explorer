package uc.seng201.environment;

import uc.seng201.gui.EndScreen;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.ObservableManager;
import uc.seng201.utils.observerable.Observer;

import javax.swing.*;

/**
 * The main game environment.
 */
public class GameEnvironment {
    /**
     * The global event manager. Observers can be registered to respond to
     * known events, as well as trigger events.
     */
    public static final ObservableManager EVENT_MANAGER =  new ObservableManager();

    /**
     * Adds the main game handlers to the event manager.
     */
    public GameEnvironment() {
        // Add handlers for outcome conditions
        EVENT_MANAGER.addObserver(Event.VICTORY, new GameEnvironment.VictoryHandler());
        EVENT_MANAGER.addObserver(Event.DEFEAT, new GameEnvironment.FailedHandler());

        // Add handler for changing game-state instance
        GameStateChangeHandler changeHandler = new GameEnvironment.GameStateChangeHandler();
        EVENT_MANAGER.addObserver(Event.NEW_GAME_STATE, changeHandler);
        EVENT_MANAGER.addObserver(Event.LOADED_GAME_STATE, changeHandler);

        // Add handler for random game events
        EVENT_MANAGER.addObserver(Event.RANDOM_EVENT, new RandomEventHandler());

        // Add handler for crew actions
        EVENT_MANAGER.addObserver(Event.CREW_MEMBER_ACTION, new ActionHandler());
    }

    /**
     * The game state to pass between objects. It is immutable and can
     * only be directly changed by an event handled by "GameStateChangeHandler".
     */
    static GameState gameState;

    /**
     * Creates an end screen instance that is either a victory or defeat. The
     * end condition message is then passed to the instance.
     *
     * @param isVictory did the user win the game.
     * @param args possible message that was passed by the event handlers.
     */
    private static void displayEndScreen(final boolean isVictory, final Object[] args) {
        String message = null;
        if (args.length == 1) {
            if (args[0] instanceof String) {
                message = (String) args[0];
            }
        }
        JDialog endScreen = new EndScreen(gameState, isVictory, message);
        endScreen.setLocationRelativeTo(null);
        endScreen.setSize(600, 350);
        endScreen.setVisible(true);

        // Exit out of the game.
        System.exit(0);
    }

    /**
     * Handles "Loaded_GAME_STATE" and "NEW_GAME_STATE" events. The global game state
     * is updated to reflect this new state, and distributed to all dependants.
     */
    static class GameStateChangeHandler implements Observer {
        @Override
        public void onEvent(final Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof GameState) {
                    gameState = (GameState) args[0];
                }
            }
        }
    }

    /**
     * Handles "VICTORY" event. When called displays the end screen that
     * displays how the user won the game.
     */
    static class VictoryHandler implements Observer {
        @Override
        public void onEvent(final Object... args) {
            displayEndScreen(true, args);
        }
    }

    /**
     * Handles "Defeat" event. When called displays the end screen that
     * displays how the user lost the game.
     */
    static class FailedHandler implements Observer {
        @Override
        public void onEvent(final Object... args) {
            displayEndScreen(false, args);
        }


    }
}
