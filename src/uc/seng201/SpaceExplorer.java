package uc.seng201;

import uc.seng201.environment.Display;
import uc.seng201.environment.GameEnvironment;

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
     * Main entry point for the application. Adds the victory, defeat and
     * game state handlers to the event manager. The display is then
     * initialized.
     *
     * @param args passed from console.
     */
    public static void main(String[] args) {
        new GameEnvironment();
        Display.setupGUI();
    }


}
