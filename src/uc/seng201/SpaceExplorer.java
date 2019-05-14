package uc.seng201;

import uc.seng201.events.RandomEventHandler;
import uc.seng201.gui.EndScreen;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.ObservableManager;
import uc.seng201.utils.observerable.Observer;

import javax.swing.*;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Main class that contains the entry point for the application. Contains the
 * end game logic and global variables related to the game state and event
 * management.
 */
public class SpaceExplorer {

    /**
     * Provides a single random generator to all classes. Using SecureRandom
     * to provide stronger randomness.
     */
    public static Random randomGenerator = new SecureRandom();
    /**
     * The game state to pass between objects. It is immutable and can
     * only been changed by an event handled by "GameStateChangeHandler".
     */
    static GameState gameState;

    /**
     * The global event manager. Observers can be registered to respond to
     * known events, as well as trigger events.
     */
    public static ObservableManager eventManager;

    /**
     * Main entry point for the application. Adds the victory, defeat and
     * game state handlers to the event manager. The display is then
     * initialized.
     *
     * @param args passed from console.
     */
    public static void main(String[] args) {
        //Create an event manager
        eventManager = new ObservableManager();
        // Add handlers for outcome conditions
        eventManager.addObserver(Event.VICTORY, new VictoryHandler());
        eventManager.addObserver(Event.DEFEAT, new FailedHandler());

        // Add handler for changing game-state instance
        GameStateChangeHandler changeHandler = new GameStateChangeHandler();
        eventManager.addObserver(Event.NEW_GAME_STATE, changeHandler);
        eventManager.addObserver(Event.LOADED_GAME_STATE, changeHandler);

        // Add handler for random game events
        eventManager.addObserver(Event.RANDOM_EVENT, new RandomEventHandler());

        Display.setupGUI();
    }


    /**
     * Handles "Loaded_GAME_STATE" and "NEW_GAME_STATE" events. The global game state
     * is updated to reflect this new state, and distributed to all dependants.
     */
    static class GameStateChangeHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
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
        public void onEvent(Object... args) {
            displayEndScreen(true, args);
        }
    }

    /**
     * Handles "Defeat" event. When called displays the end screen that
     * displays how the user lost the game.
     */
    static class FailedHandler implements Observer {
        @Override
        public void onEvent(Object... args) {
            displayEndScreen(false, args);
        }


    }

    /**
     * Creates an end screen instance that is either a victory or defeat. The
     * end condition message is then passed to the instance.
     *
     * @param isVictory did the user win the game.
     * @param args possible message that was passed by the event handlers.
     */
    private static void displayEndScreen(boolean isVictory, Object[] args) {
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
        System.exit(0);
    }
}
