package uc.seng201.gui;

import uc.seng201.environment.GameState;

public enum Screen {
    MAIN_MENU() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new MainMenu(gameState);
        }
    },
    MAIN_SCREEN() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new MainScreen(gameState);
        }
    },
    ADVENTURE_CREATOR() {
        @Override
        public ScreenComponent createInstance(GameState gameState) {
            return new AdventureCreator(gameState);
        }
    };


    public abstract ScreenComponent createInstance(GameState gameState);

}
