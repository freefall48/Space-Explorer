package uc.seng201.gui;

import uc.seng201.environment.Display;
import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.utils.SavedGameFileFilter;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.IOException;

/**
 * Provides the load or new game options.
 */
class MainMenu extends ScreenComponent {

    /**
     * Root panel
     */
	private JPanel Menu;
    /**
     * Button to create a new game.
     */
    private JButton btnNewGame;
    /**
     * Button to load an existing game
     *
     */
    private JButton btnLoadGame;

    /**
     * Main menu to ask the user if they want to create a new game or load an existing game.
     *
     * @param gameState null as not required for this screen.
     */
    MainMenu(GameState gameState) {

        btnLoadGame.addActionListener(e -> onLoadGame());
        btnNewGame.addActionListener(e -> onNewGame());

    }

    @Override
    public JComponent getRootComponent() {
        return Menu;
    }

    /**
     * When the user wants to create a new game switches to the correct screen.
     */
    private void onNewGame() {
        Display.changeScreen(Screen.ADVENTURE_CREATOR);
    }

    /**
     * When the user wants to load a game. Provides a popup dialog where the user can
     * select a game save file.
     */
    private void onLoadGame() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileFilter(new SavedGameFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        int success = fileChooser.showOpenDialog(this);
        if (success == JFileChooser.APPROVE_OPTION) {
            // The user has given us a file so try and load it.
            try {
                // Let the event manager know that there is a new game state.
                GameEnvironment.eventManager.notifyObservers(Event.LOADED_GAME_STATE,
                        GameState.loadState(fileChooser.getSelectedFile().getAbsolutePath()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to load file",
                        "Error", JOptionPane.ERROR_MESSAGE);
                // Handles the crash and goes back to the main menu. The user can choose to load or new game again.
                e.printStackTrace();
                return;
            }
            Display.changeScreen(Screen.MAIN_SCREEN);
        }
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
