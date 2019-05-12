package uc.seng201;

import uc.seng201.gui.Screen;
import uc.seng201.gui.ScreenComponent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Display {

    private static JFrame rootFrame;
    private static Map<Screen, ScreenComponent> screens;

    public static void changeScreen(Screen screen, boolean isStale) {
        ScreenComponent component = screens.get(screen);
        if (component == null) {
            component = screen.createInstance(SpaceExplorer.gameState);
            screens.replace(screen, component);
        }
        if (isStale) {
            screens.forEach((key, value) -> screens.put(key, null));
        }
        rootFrame.setContentPane(component.getRootComponent());
        rootFrame.pack();
        rootFrame.repaint();

    }

    public static void changeScreen(Screen screen) {
        changeScreen(screen, false);
    }

    public static JFrame getRootFrame() {
        return rootFrame;
    }

    public static void popup(String message) {
        JOptionPane.showMessageDialog(rootFrame, message);
    }

    static void setupGUI() {
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
