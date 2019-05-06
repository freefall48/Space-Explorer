package uc.seng201;

import uc.seng201.gui.EndScreen;
import uc.seng201.gui.Screen;
import uc.seng201.gui.ScreenComponent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SpaceExplorer {

    private JFrame rootFrame;
    private GameState gameState;
    private Map<Screen, ScreenComponent> screens;

    private static SpaceExplorer spaceExplorer;


    public void changeScreen(Screen screen, boolean isStale) {
        ScreenComponent component = screens.get(screen);
        if (component == null) {
            component = screen.createInstance(this);
            screens.replace(screen, component);
        }
        if (isStale) {
            screens.forEach((key, value) -> screens.put(key, null));
        }
        rootFrame.setContentPane(component.getRootComponent());
        rootFrame.pack();
        rootFrame.repaint();
    }
    public void changeScreen(Screen screen) {
        changeScreen(screen, false);
    }

    public JFrame getRootFrame() {
        return this.rootFrame;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public static void main(String[] args) {
        spaceExplorer = new SpaceExplorer();
        spaceExplorer.setupGUI();
    }

    private void setupGUI() {
        rootFrame = new JFrame("Space Explorer");
        rootFrame.setSize(800, 600);
        rootFrame.setResizable(false);
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootFrame.setLocationRelativeTo(null);

        screens = new HashMap<>();
        screens.put(Screen.MAIN_MENU, null);
        screens.put(Screen.MAIN_SCREEN, null);
        screens.put(Screen.ADVENTURE_CREATOR, null);

        changeScreen(Screen.MAIN_MENU);
        rootFrame.setVisible(true);
    }

    /**
     * @param message Message to be displayed.
     */
    public static void endGame(String message, boolean isVictory) {
        JDialog endScreen = new EndScreen(spaceExplorer.gameState, isVictory, message);
        endScreen.setLocationRelativeTo(spaceExplorer.getRootFrame());
        endScreen.setSize(600, 350);
        endScreen.setVisible(true);
        spaceExplorer.changeScreen(Screen.MAIN_MENU, true);
    }
}
