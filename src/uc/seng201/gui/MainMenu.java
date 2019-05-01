package uc.seng201.gui;

import uc.seng201.GameState;
import uc.seng201.SpaceExplorerGui;
import uc.seng201.helpers.StateActions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JComponent {
    private JPanel Menu;
    private JButton btnNewGame;
    private JButton btnLoadGame;

    public MainMenu() {
        btnLoadGame.addActionListener(e -> onLoadGame());
        btnNewGame.addActionListener(e -> SpaceExplorerGui.redrawRoot(new AdventureCreator().$$$getRootComponent$$$()));
    }

    private void onLoadGame() {
        FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(), "Choose a file", FileDialog.LOAD);
        fd.setFile("*.json");
        fd.setFilenameFilter((dir, name) -> name.toUpperCase().endsWith(".JSON"));
        fd.setMultipleMode(false);
        fd.setLocationRelativeTo(SpaceExplorerGui.getControlFrame());
        fd.setVisible(true);
        if (fd.getFile() != null) {
            try {
                GameState gameState = StateActions.loadState(fd.getDirectory() + fd.getFile());
                SpaceExplorerGui.spaceShip = gameState.getSpaceShip();
                SpaceExplorerGui.gameDuration = gameState.getDuration();
                SpaceExplorerGui.currentDay = gameState.getCurrentDay();
                SpaceExplorerGui.currentPlanet = gameState.getCurrentPlanet();
                SpaceExplorerGui.planets = gameState.getPlanets();
                SpaceExplorerGui.shipImageLocation = gameState.getShipImage();
                if (SpaceExplorerGui.shipImageLocation != null) {
                    SpaceExplorerGui.shipImage = ImageIO.read(new File(SpaceExplorerGui.shipImageLocation));
                }
                SpaceExplorerGui.redrawRoot(new MainScreen().$$$getRootComponent$$$());
            } catch (IOException error) {
                JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                        "Failed to load the selected saved game!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        fd.dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Menu = new JPanel();
        Menu.setLayout(new GridBagLayout());
        Menu.setFocusCycleRoot(false);
        Menu.setMinimumSize(new Dimension(600, 400));
        Menu.setPreferredSize(new Dimension(600, 400));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 48, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Space Explorer");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        Menu.add(label1, gbc);
        btnLoadGame = new JButton();
        btnLoadGame.setActionCommand("SLOT3");
        btnLoadGame.setEnabled(true);
        Font btnLoadGameFont = this.$$$getFont$$$("Droid Sans Mono", -1, 20, btnLoadGame.getFont());
        if (btnLoadGameFont != null) btnLoadGame.setFont(btnLoadGameFont);
        btnLoadGame.setText("Load Game");
        btnLoadGame.setToolTipText("No save game avaliable in this slot!");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.insets = new Insets(10, 40, 0, 40);
        Menu.add(btnLoadGame, gbc);
        btnNewGame = new JButton();
        btnNewGame.setActionCommand("NEW");
        btnNewGame.setEnabled(true);
        Font btnNewGameFont = this.$$$getFont$$$("Droid Sans Mono", -1, 20, btnNewGame.getFont());
        if (btnNewGameFont != null) btnNewGame.setFont(btnNewGameFont);
        btnNewGame.setSelected(false);
        btnNewGame.setText("New Game");
        btnNewGame.setToolTipText("New Game");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.insets = new Insets(30, 40, 0, 40);
        Menu.add(btnNewGame, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Menu;
    }
}
