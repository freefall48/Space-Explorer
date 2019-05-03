package uc.seng201;

import uc.seng201.gui.MainMenu;
import uc.seng201.targets.Planet;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SpaceExplorer {


    private static JFrame controlFrame;
    public static BufferedImage shipImage;
    private static GameState gameState;

    public static JFrame getControlFrame() {
        return controlFrame;
    }



    public static GameState getGameState() {
        return gameState;
    }

    public static void main(String[] args) {

        controlFrame = new JFrame("Space Explorer");
        controlFrame.setContentPane(new MainMenu().$$$getRootComponent$$$());
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setResizable(false);
        controlFrame.pack();
        controlFrame.setLocationRelativeTo(SpaceExplorer.getControlFrame());
        controlFrame.setVisible(true);
    }

    public static void redrawRoot(JComponent panel) {
        controlFrame.setContentPane(panel);
        controlFrame.pack();
    }

    public static void popup(String message) {
        JOptionPane.showMessageDialog(controlFrame, message);
    }

    public static void failedGame(String message) {
        JOptionPane.showMessageDialog(controlFrame, message, "Failed Game", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    }

    public static void completedGame() {
        JOptionPane.showMessageDialog(controlFrame, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static Planet getPlanetFromName(String planetName) {
        for (Planet planet : planets) {
            if (planet.getPlanetName().equals(planetName)) {
                return planet;
            }
        }
        return null;
    }
}
