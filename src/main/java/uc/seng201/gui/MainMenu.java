package uc.seng201.gui;

import uc.seng201.GameState;
import uc.seng201.helpers.StateActions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel implements Screen{
    private JPanel Menu;
    private JButton btnNewGame;
    private JButton btnLoadGame;

    MainMenu() {
        btnLoadGame.addActionListener(e -> {
            FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(), "Choose a file", FileDialog.LOAD);
            fd.setFile("*.json");
            fd.setFilenameFilter((dir, name) -> name.toUpperCase().endsWith(".JSON"));
            fd.setMultipleMode(false);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                try {
                   GameState gameState = StateActions.loadState(fd.getDirectory()+fd.getFile());
                   SpaceExplorerGui.spaceShip = gameState.getSpaceShip();
                   SpaceExplorerGui.gameDuration = gameState.getDuration();
                    SpaceExplorerGui.currentDay = gameState.getCurrentDay();
                    SpaceExplorerGui.currentPlanet = gameState.getCurrentPlanet();
                    SpaceExplorerGui.planets = gameState.getPlanets();
                    SpaceExplorerGui.shipImageLocation = gameState.getShipImage();
                    if (SpaceExplorerGui.shipImageLocation != null) {
                        SpaceExplorerGui.shipImage = ImageIO.read(new File(SpaceExplorerGui.shipImageLocation));
                    }
                    SpaceExplorerGui.redraw(new MainScreen().getRootPanel());
                } catch (IOException error) {
                    JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                            "Failed to load the selected saved game!","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            fd.dispose();
        });
        btnNewGame.addActionListener(e -> SpaceExplorerGui.redraw(new AdventureCreator().getRootPanel()));
    }

    @Override
    public JPanel getRootPanel() {
        return this.Menu;
    }
}
