package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.StateActions;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.io.IOException;

public class MainScreen implements Screen {

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
    private JButton btnNextDay;

    private static DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();

    public JPanel getRootPanel() {
        return panelRoot;
    }

    MainScreen() {
        updateHUD();

        updateTabs();

        btnNextDay.addActionListener(e -> onNextDay());

        listCrew.addListSelectionListener(this::onCrewMemberSelection);

        btnPerformAction.addActionListener(e -> onPerformAction());

        btnSave.addActionListener(e -> onSave());
    }

    private void onSave() {
        FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(),"Save", FileDialog.SAVE);
        fd.setFilenameFilter((dir, name) -> name.toUpperCase().endsWith(".JSON"));
        fd.setVisible(true);
        try {
            if (fd.getFile() != null) {
                StateActions.saveState(fd.getDirectory()+fd.getFile(), SpaceExplorerGui.spaceShip,
                        SpaceExplorerGui.planets, SpaceExplorerGui.currentPlanet, SpaceExplorerGui.currentDay,
                        SpaceExplorerGui.gameDuration, SpaceExplorerGui.shipImageLocation);
            } else {
                throw new IOException();
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(), "Failed to save or was cancelled!",
                    "Could not save", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onNextDay() {
        boolean showActionsLeftWarning = false;
        for (int i = 0; i < listCrewModal.getSize(); i++) {
            if (listCrewModal.get(i).canPerformActions()) {
                showActionsLeftWarning = true;
            }
        }
        if (showActionsLeftWarning) {
            int confirmed = JOptionPane.showConfirmDialog(SpaceExplorerGui.getControlFrame(), "Do you really want to move to the next day?" +
                    " Some crew members can still perform actions!", "Actions Left Today", JOptionPane.YES_NO_OPTION);
            if (confirmed != 0) {
                return;
            }
        }
        int nextDay = SpaceExplorerGui.currentDay + 1;
        if (nextDay <=  SpaceExplorerGui.gameDuration) {
            SpaceExplorerGui.currentDay = nextDay;
            updateHUD();
        } else {
            JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(), "On no! It seems" +
                    "you have failed to rebuild your ship! Err....", "Failure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private void onPerformAction() {
        JDialog performAction = new PerformAction(listCrewModal.get(listCrew.getSelectedIndex()));
        performAction.setSize(450,350);
        performAction.setLocationRelativeTo(null);
        performAction.setVisible(true);
        reloadState();
        listCrew.clearSelection();
        btnPerformAction.setEnabled(false);
    }

    private void onCrewMemberSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && listCrew.getSelectedIndex() != -1) {
            if (listCrewModal.get(listCrew.getSelectedIndex()).canPerformActions()) {
                btnPerformAction.setEnabled(true);
            } else {
                btnPerformAction.setEnabled(false);
            }
        }
    }

    private void updateTabs() {
        listCrewModal.addAll(SpaceExplorerGui.spaceShip.getShipCrew());
        listCrew.setModel(listCrewModal);
    }

    private void updateHUD() {
        lblSpaceShipName.setText(SpaceExplorerGui.spaceShip.getShipName());
        lblDay.setText(String.format("%d of %d", SpaceExplorerGui.currentDay, SpaceExplorerGui.gameDuration));
        lblOrbiting.setText(SpaceExplorerGui.currentPlanet.toString());
        lblMissingParts.setText(String.valueOf(SpaceExplorerGui.spaceShip.getMissingParts()));
        lblBalance.setText("$" + SpaceExplorerGui.spaceShip.getSpaceBucks());
    }

    private void reloadState() {
        panelRoot.revalidate();
        panelRoot.repaint();
    }
}
