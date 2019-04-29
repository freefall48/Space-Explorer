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

    static void redraw(JPanel panel) {
        controlFrame.setContentPane(panel);
        controlFrame.pack();
    }

    public static void popup(String message) {
        JOptionPane.showMessageDialog(controlFrame, message);
    }
}
