package uc.seng201;

import uc.seng201.events.RandomEventHandler;
import uc.seng201.gui.EndScreen;
import uc.seng201.utils.observerable.Event;
import uc.seng201.utils.observerable.ObservableHandler;
import uc.seng201.utils.observerable.Observer;

import javax.swing.*;

public class SpaceExplorer {

    static GameState gameState;

    public static ObservableHandler eventHandler = new EventHandler();

    public static void main(String[] args) {
        // Add handlers for outcome conditions
        eventHandler.addObserver(Event.VICTORY, new Victory());
        eventHandler.addObserver(Event.DEFEAT, new Failed());

        // Add handler for changing game-state instance
        eventHandler.addObserver(Event.NEW_GAMESTATE, new NewGameState());

        // Add handler for random game events
        eventHandler.addObserver(Event.RANDOM_EVENT, new RandomEventHandler());

        Display.setupGUI();
    }


    static class NewGameState implements Observer {
        @Override
        public void onEvent(Object... args) {
            if (args.length == 1) {
                if (args[0] instanceof GameState) {
                    gameState = (GameState) args[0];
                }
            }
        }
    }

    static class Victory implements Observer {
        @Override
        public void onEvent(Object... args) {
            displayEndScreen(true, args);
        }
    }

    static class Failed implements Observer {
        @Override
        public void onEvent(Object... args) {
            displayEndScreen(false, args);
        }


    }

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
