package uc.seng201.gui;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenuScreen extends JPanel implements Screen {
    private JPanel Menu;
    private JButton slot3Button;
    private JButton slot1Button;
    private JButton slot2Button;

    MainMenuScreen() {

        checkSaves();
        slot1Button.addActionListener(e -> {
            SpaceExplorerGui.redraw(new StatsScreen().getScreen());
        });
        slot2Button.addActionListener(e -> {

        });
        slot3Button.addActionListener(e -> {
        });
    }

    @Override
    public JPanel getScreen() {
        return this.Menu;
    }

    private void checkSaves() {
        try {
            if (Files.notExists(Paths.get("saves"))) {
                Files.createDirectory(Paths.get("saves"));
                this.slot1Button.setText("New Game");
                this.slot1Button.setEnabled(true);
            } else {
                if (Files.exists(Paths.get("saves/save1.json"))) this.slot1Button.setEnabled(true);
                if (Files.exists(Paths.get("saves/save2.json"))) this.slot2Button.setEnabled(true);
                if (Files.exists(Paths.get("saves/save3.json"))) this.slot3Button.setEnabled(true);
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }


}
