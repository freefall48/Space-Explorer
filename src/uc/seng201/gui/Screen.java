package uc.seng201.gui;

import uc.seng201.SpaceExplorer;

public enum Screen {
    MAIN_MENU() {
        @Override
        public ScreenComponent createInstance(SpaceExplorer spaceExplorer) {
            return new MainMenu(spaceExplorer);
        }
    },
    MAIN_SCREEN() {
        @Override
        public ScreenComponent createInstance(SpaceExplorer spaceExplorer) {
            return new MainScreen(spaceExplorer);
        }
    },
    ADVENTURE_CREATOR() {
        @Override
        public ScreenComponent createInstance(SpaceExplorer spaceExplorer) {
            return new AdventureCreator(spaceExplorer);
        }
    };


    public abstract ScreenComponent createInstance(SpaceExplorer spaceExplorer);

}
