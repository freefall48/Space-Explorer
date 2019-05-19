package uc.seng201.environment;

import uc.seng201.gui.Screen;
import uc.seng201.gui.ScreenComponent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls the root component that is displayed to the screen. Provides
 * the ability to change the main component.
 */
public class Display {

    /**
     * The root frame.
     */
    private static JFrame rootFrame;

    /**
     * Map of the known screes to instances of them. Using
     * a map only one instance of a screen is needed no matter
     * how many times it is displayed.
     */
    private static Map<Screen, ScreenComponent> screens;

    /**
     * Changes the main screen component that is being displayed. Replaces
     * the instance of the screens if needed.
     *
     * @param screen the new Screen to show.
     * @param reinitialise true if the screen needs to be replaced.
     */
    public static void changeScreen(Screen screen, boolean reinitialise) {
        ScreenComponent component = screens.get(screen);
        if (reinitialise) {
            screens.forEach((key, value) -> screens.put(key, key.createInstance(GameEnvironment.gameState)));
        }
        if (component == null) {
            component = screen.createInstance(GameEnvironment.gameState);
            screens.replace(screen, component);
        }
        rootFrame.setContentPane(component.getRootComponent());
        rootFrame.pack();
        rootFrame.repaint();

    }

    /**
     * Returns the root JFrame.
     *
     * @return the root JFrame.
     */
    public static JFrame getRootFrame() {
        return rootFrame;
    }

    /**
     * Changes the main screen component that is being displayed.
     *
     * @param screen the new Screen to show.
     */
    public static void changeScreen(Screen screen) {
        changeScreen(screen, false);
    }

    /**
     * Provides the ability to display a popup centered on the
     * screen that is currently being displayed.
     *
     * @param message to be displayed within the window.
     */
    public static void popup(String message) {
        JOptionPane.showMessageDialog(rootFrame, message);
    }

    /**
     * Initialises the root frame and adds all known screens to the
     * screen map. The main menu screen is then displayed.
     */
    public static void setupGUI() {
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
}
