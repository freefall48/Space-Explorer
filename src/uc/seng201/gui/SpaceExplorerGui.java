package uc.seng201.gui;

import uc.seng201.SpaceShip;
import uc.seng201.targets.Planet;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SpaceExplorerGui {


    private static JFrame controlFrame;

    static int gameDuration;
    static int currentDay = 1;
    static SpaceShip spaceShip;
    static List<Planet> planets;
    static Planet currentPlanet;
    static BufferedImage shipImage;
    static String shipImageLocation;

    static JFrame getControlFrame() {
        return controlFrame;
    }

    public static void main(String[] args) {

        controlFrame = new JFrame("Space Explorer");
        controlFrame.setContentPane(new MainMenu().getRootPanel());
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setResizable(false);
        controlFrame.pack();
        controlFrame.setLocationRelativeTo(null);
        controlFrame.setVisible(true);
    }

    static void redrawRoot(JPanel panel) {
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

    static Planet getPlanet(String planetName) {
        for (Planet planet : planets) {
            if (planet.getPlanetName().equals(planetName)) {
                return planet;
            }
        }
        return null;
    }
}
