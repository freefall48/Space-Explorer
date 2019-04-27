package uc.seng201.gui;

import javax.swing.*;

public class SpaceExplorerGui {


    private static JFrame controlFrame;

    public static void main(String[] args) {

        controlFrame = new JFrame("Space Explorer");
        controlFrame.setContentPane(new MainMenuScreen().getScreen());
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setResizable(false);
        controlFrame.pack();
        controlFrame.setVisible(true);
    }

    static void redraw(JPanel panel) {
        controlFrame.setContentPane(panel);
        controlFrame.pack();
    }
}
