package uc.seng201.gui;

import uc.seng201.environment.GameState;

/**
 * Enum for the main panels of the game.
 */
public enum Screen {
    /**
     * The main menu provides the ability to load a game or start a new game.
     */
    MAIN_MENU() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new MainMenu(gameState);
        }
    },
    /**
     * The main screen is the main game screen. It shows an overview and allows
     * the user to interact with their crew and traders.
     */
    MAIN_SCREEN() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new MainScreen(gameState);
        }
    },
    /**
     * Allows the user to create a new game with their crew and spaceship.
     */
    ADVENTURE_CREATOR() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new AdventureCreator(gameState);
        }
    };

    /**
     * Returns an instance of the correct screen based on the enum type.
     *
     * @param gameState reference to the current game state.
     * @return screen instance for the corresponding enum type.
     */
    public abstract ScreenComponent createInstance(GameState gameState);

}
