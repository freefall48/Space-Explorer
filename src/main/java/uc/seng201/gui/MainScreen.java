package uc.seng201.gui;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.StateActions;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.temporal.ValueRange;

public class MainScreen extends JPanel implements Screen {

    private JPanel panelRoot;
    private JLabel lblSpaceShipName;
    private JTabbedPane tabbedPane1;
    private JList listCrew;
    private JLabel lblDay;
    private JLabel lblOrbiting;
    private JLabel lblMissingParts;
    private JLabel lblBalance;
    private JButton btnPerformAction;
    private JButton btnSave;

    private DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();

    public JPanel getRootPanel() {
        return panelRoot;
    }

    MainScreen() {
        lblSpaceShipName.setText(SpaceExplorerGui.spaceShip.getShipName());
        lblDay.setText(String.valueOf(SpaceExplorerGui.currentDay));
        lblOrbiting.setText(SpaceExplorerGui.currentPlanet.toString());
        lblMissingParts.setText(String.valueOf(SpaceExplorerGui.spaceShip.getMissingParts()));
        lblBalance.setText("$" + SpaceExplorerGui.spaceShip.getSpaceBucks());

        listCrewModal.addAll(SpaceExplorerGui.spaceShip.getShipCrew());
        listCrew.setModel(listCrewModal);

        btnSave.addActionListener(e -> {
            FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(),"Save", FileDialog.SAVE);
            fd.setFilenameFilter((dir, name) -> name.toUpperCase().endsWith(".JSON"));
            fd.setVisible(true);
            try {
                if (!fd.getFile().equals("")) {
                    StateActions.saveState(fd.getDirectory()+fd.getFile(), SpaceExplorerGui.spaceShip,
                            SpaceExplorerGui.planets, SpaceExplorerGui.currentPlanet, SpaceExplorerGui.currentDay,
                            SpaceExplorerGui.gameDuration, SpaceExplorerGui.shipImageLocation);
                } else {
                    throw new IOException();
                }
            } catch (IOException error) {
                JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(), "Failed to save!",
                        "Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
